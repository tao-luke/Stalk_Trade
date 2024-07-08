import json
import requests
import firebase_admin
from firebase_admin import credentials, firestore, messaging
from datetime import datetime, timedelta
import os

from trade import * 
from util import *

current_dir = os.path.dirname(os.path.realpath(__file__))
json_file = 'stalk-db-firebase-adminsdk-g64um-cd9cc7e2a2.json'
json_path = os.path.join(current_dir, json_file)

# Initialize Firebase Admin SDK with the service account key
cred = credentials.Certificate(json_path)
firebase_admin.initialize_app(cred)

db = firestore.client()

# Function to fetch trading data from external API
def fetch_data_senate_trading(page):
    url = f"https://financialmodelingprep.com/api/v4/senate-trading-rss-feed?page={page}&apikey=ae3hqrlXlU4y481FMMLugIrqOJMEQHAg"
    response = requests.get(url)
    return response.json()

# Function to fetch trading data from external API
def fetch_data_senate_disclosure(page):
    url = f"https://financialmodelingprep.com/api/v4/senate-disclosure-rss-feed?page={page}&apikey=ae3hqrlXlU4y481FMMLugIrqOJMEQHAg"
    response = requests.get(url)
    return response.json()

# Function to upload trade data to Firestore
def upload_trade_data_to_firestore(data, collection):
    new = []
    one_year_ago = datetime.now() - timedelta(days=365)
    db = firestore.client()
    collection_ref = db.collection(collection)
    
    if isinstance(data, dict):
        # Calculate hash of content
        content_hash = calculate_content_hash(str(data))
        # Check if document with the same hash already exists
        if not document_exists(collection_ref, content_hash):
            # Parse the transaction date
            date_str = data.get('transactionDate')
            if date_str:
                try:
                    date_obj = datetime.strptime(date_str, '%Y-%m-%d')
                    if date_obj >= one_year_ago:
                        # Add hash to data
                        data["hash"] = content_hash
                        # Add data to Firestore
                        collection_ref.add(data)
                        new.append(data)
                        print("New trade added: ", data)
                    else:
                        print(f"Skipping old trade: {data}")
                except ValueError as e:
                    print(f"::error::Error parsing date: {e}")
            else:
                print(f"::error::No transaction date found in data: {data}")

    elif isinstance(data, list):
        for entry in data:
            # Calculate hash of content
            content_hash = calculate_content_hash(str(entry))
            # Check if document with the same hash already exists
            if not document_exists(collection_ref, content_hash):
                # Parse the transaction date
                date_str = entry.get('transactionDate')
                if date_str:
                    try:
                        date_obj = datetime.strptime(date_str, '%Y-%m-%d')
                        if date_obj >= one_year_ago:
                            # Add hash to entry
                            entry["hash"] = content_hash
                            # Add entry to Firestore
                            collection_ref.add(entry)
                            new.append(entry)
                            print("New trade added: ", entry)
                        else:
                            print(f"Skipping old trade: {entry}")
                    except ValueError as e:
                        print(f"::error::Error parsing date: {e}")
                else:
                    print(f"::error::No transaction date found in entry: {entry}")

    return new

# Function to upload unique names to Firestore
def upload_names_to_firestore(data):
    names_collection_ref = db.collection("names")
    
    # Extract and upload unique names
    for entry in data:
        first_name = entry.get("firstName")
        last_name = entry.get("lastName")
        if first_name and last_name and not name_exists(names_collection_ref, first_name, last_name):
            name_data = {
                "firstName": first_name,
                "lastName": last_name
            }
            names_collection_ref.add(name_data)

            print(f"::debug::Adding name: {first_name} {last_name}")

def delete_old_entries(collection):
    one_year_ago = datetime.now() - timedelta(days=365)
    collection_ref = db.collection(collection)
    docs = collection_ref.stream()
    
    for doc in docs:
        doc_data = doc.to_dict()
        date_str = doc_data.get('transactionDate')
        
        if date_str:
            try:
                date_obj = datetime.strptime(date_str, '%Y-%m-%d')
                if date_obj < one_year_ago:
                    # print(f"Deleting document ID: {doc.id} with date: {date_str}")
                    collection_ref.document(doc.id).delete()
            except ValueError as e:
                print(f"::error::Error parsing date for document ID: {doc.id} - {e}")

def remove_unused_names():
    names_collection_ref = db.collection("names")
    all_trades_collection_ref = db.collection("all_trades")
    
    names_docs = names_collection_ref.stream()
    
    for name_doc in names_docs:
        name_data = name_doc.to_dict()
        first_name = name_data.get("firstName")
        last_name = name_data.get("lastName")
        
        trades_query = all_trades_collection_ref.where("firstName", "==", first_name).where("lastName", "==", last_name).limit(1).get()
        
        if len(trades_query) == 0:
            print("Deleting unused name: {first_name} {last_name}")
            names_collection_ref.document(name_doc.id).delete()

def update(collection):
    new_trades = []

    data = fetch_data_senate_disclosure(0)
    parsed_data = parse_senate_disclosure(data)
    new_trades.append(upload_trade_data_to_firestore(parsed_data, collection))
    upload_names_to_firestore(parsed_data)

    data = fetch_data_senate_trading(0)
    parsed_data = parse_senate_trading(data)
    new_trades.append(upload_trade_data_to_firestore(parsed_data, collection))
    upload_names_to_firestore(parsed_data)

    remove_unused_names()

    print("::debug::Total of " + str(len(new_trades)) + " new trades added")

def fetch_fcm_tokens():
    tokens = []

    # Fetch tokens from Firestore
    collection_ref = db.collection('fcm_tokens')
    docs = collection_ref.get()

    for doc in docs:
        tokens.append(doc.to_dict().get('token'))

    return tokens

def send_data_message(tokens, trades_data):
    # Convert the trades_data array to a JSON string
    trades_data_json = json.dumps(trades_data)

    # Construct the data message payload
    data_message = messaging.MulticastMessage(
        data={
            'trades_data': trades_data_json
        },
        tokens=tokens
    )

    # Send the message
    response = messaging.send_multicast(data_message)
    print(f'Successfully sent message: {response.success_count} messages were sent successfully')
    
    if response.failure_count > 0:
        responses = response.responses
        failed_tokens = []
        for idx, resp in enumerate(responses):
            if not resp.success:
                # The token is invalid, log the token and error message
                failed_tokens.append(tokens[idx])
                print(f'Token {tokens[idx]} failed: {resp.exception}')
        remove_invalid_tokens(failed_tokens)

def remove_invalid_tokens(tokens):
    for token in tokens:
        db.collection('device_tokens').document(token).delete()

def main():
    update("all_trades")

if __name__ == "__main__":
    main()

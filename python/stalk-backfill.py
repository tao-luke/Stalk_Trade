import json
import requests
import firebase_admin
from firebase_admin import credentials, firestore
import hashlib
import logging
from datetime import datetime, timedelta
import os

current_dir = os.path.dirname(os.path.realpath(__file__))
json_file = 'stalk-db-firebase-adminsdk-g64um-cd9cc7e2a2.json'
json_path = os.path.join(current_dir, json_file)

# Initialize Firebase Admin SDK with the service account key
cred = credentials.Certificate(json_path)
firebase_admin.initialize_app(cred)

annotations = []

class Trade:
    def __init__(self, firstName, lastName, transactionDate, owner, assetDescription, type, amount, link, dateRecieved, ticker):
        self.firstName = firstName
        self.lastName = lastName
        self.transactionDate = transactionDate
        self.owner = owner
        self.assetDescription = assetDescription
        self.type = type
        self.amount = amount
        self.link = link
        self.dateRecieved = dateRecieved
        self.ticker = ticker

def parse_senate_trading(data):
    trades = []
    for item in data:
        trade = Trade(
            firstName=item.get("firstName"),
            lastName=item.get("lastName"),
            transactionDate=item.get("transactionDate"),
            owner=item.get("owner"),
            assetDescription=item.get("assetDescription"),
            type=item.get("type"),
            amount=item.get("amount"),
            link=item.get("link"),
            dateRecieved=item.get("dateRecieved"),
            ticker=item.get("symbol")
        )
        trades.append(trade.__dict__)  # Convert Trade object to dictionary
    return trades

def parse_senate_disclosure(data):
    trades = []
    for item in data:
        trade = Trade(
            firstName=item.get("representative").split(" ", 1)[0],
            lastName=item.get("representative").split(" ", 1)[1],
            transactionDate=item.get("transactionDate"),
            owner=item.get("owner"),
            assetDescription=item.get("assetDescription"),
            type=item.get("type"),
            amount=item.get("amount"),
            link=item.get("link"),
            dateRecieved=item.get("disclosureDate"),
            ticker=item.get("ticker")
        )
        trades.append(trade.__dict__)  # Convert Trade object to dictionary
    return trades

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

# Function to calculate hash of content
def calculate_content_hash(content):
    return hashlib.sha256(content.encode()).hexdigest()

# Function to check if document with the same hash exists
def document_exists(collection_ref, content_hash):
    query = collection_ref.where("hash", "==", content_hash).limit(1).get()
    return len(query) > 0

# Function to check if a name already exists in the names collection
def name_exists(collection_ref, first_name, last_name):
    query = collection_ref.where("firstName", "==", first_name).where("lastName", "==", last_name).limit(1).get()
    return len(query) > 0

# Function to upload trade data to Firestore
def upload_trade_data_to_firestore(data, collection):
    count = 0
    db = firestore.client()
    collection_ref = db.collection(collection)
    # Assuming data is a dictionary or list of dictionaries
    if isinstance(data, dict):
        # Calculate hash of content
        content_hash = calculate_content_hash(str(data))
        # Check if document with the same hash already exists
        if not document_exists(collection_ref, content_hash):
            # Add hash to data
            data["hash"] = content_hash
            # Add data to Firestore
            collection_ref.add(data)
            count += 1
    elif isinstance(data, list):
        for entry in data:
            # Calculate hash of content
            content_hash = calculate_content_hash(str(entry))
            # Check if document with the same hash already exists
            if not document_exists(collection_ref, content_hash):
                # Add hash to entry
                entry["hash"] = content_hash
                # Add entry to Firestore
                collection_ref.add(entry)
                count += 1
    return count

# Function to upload unique names to Firestore
def upload_names_to_firestore(data):
    db = firestore.client()
    names_collection_ref = db.collection("names")
    
    # Extract and upload unique names
    for entry in data:
        first_name = entry.get("firstName")
        last_name = entry.get("lastName")
        if first_name and last_name and not name_exists(names_collection_ref, first_name, last_name):
            name_data = {
                "firstName": first_name,
                "lastName": last_name,
                "performance": 0
            }
            names_collection_ref.add(name_data)

            add_debug(f"::debug::Adding name: {first_name} {last_name}")

def delete_old_entries(collection):
    one_year_ago = datetime.now() - timedelta(days=365)
    db = firestore.client()
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
                add_error(f"::error::Error parsing date for document ID: {doc.id} - {e}")

def remove_unused_names():
    db = firestore.client()
    names_collection_ref = db.collection("names")
    all_trades_collection_ref = db.collection("all_trades")
    
    names_docs = names_collection_ref.stream()
    
    for name_doc in names_docs:
        name_data = name_doc.to_dict()
        first_name = name_data.get("firstName")
        last_name = name_data.get("lastName")
        
        trades_query = all_trades_collection_ref.where("firstName", "==", first_name).where("lastName", "==", last_name).limit(1).get()
        
        if len(trades_query) == 0:
            add_debug("Deleting unused name: {first_name} {last_name}")
            names_collection_ref.document(name_doc.id).delete()

def update(collection):
    data = fetch_data_senate_disclosure(0)
    parsed_data = parse_senate_disclosure(data)
    new_trades1 = upload_trade_data_to_firestore(parsed_data, collection)
    upload_names_to_firestore(parsed_data)

    data = fetch_data_senate_trading(0)
    parsed_data = parse_senate_trading(data)
    new_trades2 = upload_trade_data_to_firestore(parsed_data, collection)
    upload_names_to_firestore(parsed_data)

    delete_old_entries(collection)

    remove_unused_names()

    add_debug("Total of " + str(new_trades1 + new_trades2) + " new trades added")

def add_error(message):
    annotations.append({
        "path": "",
        "start_line": 0,
        "end_line": 0,
        "annotation_level": "error",
        "message": message
    })

def add_debug(message):
    annotations.append({
        "path": "",
        "start_line": 0,
        "end_line": 0,
        "annotation_level": "debug",
        "message": message
    })

def main():
    update("all_trades")

    # Combine all annotations into one structure
    output_data = {
        "annotations": annotations
    }

    # Print JSON formatted data
    print(json.dumps(output_data))

if __name__ == "__main__":
    main()

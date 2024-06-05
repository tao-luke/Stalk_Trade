import requests
import firebase_admin
from firebase_admin import credentials, firestore
import hashlib
import time

# Initialize Firebase Admin SDK
cred = credentials.Certificate("stalk-db-firebase-adminsdk-g64um-cd9cc7e2a2.json")
firebase_admin.initialize_app(cred)

# Function to fetch data from external API
def fetch_data_senate_trading(page):
    url = "https://financialmodelingprep.com/api/v4/senate-trading-rss-feed?page=" + str(page) + "&apikey=ae3hqrlXlU4y481FMMLugIrqOJMEQHAg"
    response = requests.get(url)
    return response.json()

# Function to calculate hash of content
def calculate_content_hash(content):
    return hashlib.sha256(content.encode()).hexdigest()

# Function to check if document with the same hash exists
def document_exists(collection_ref, content_hash):
    query = collection_ref.where("hash", "==", content_hash).limit(1).get()
    return len(query) > 0

# Function to upload data to Firestore
def upload_data_to_firestore(data):
    db = firestore.client()
    collection_ref = db.collection("trades")
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

# Main function to run periodically
def main():
    # Fetch data from API
    data = fetch_data_senate_trading(0)
    
    # Upload data to Firestore
    upload_data_to_firestore(data)


if __name__ == "__main__":
    main()

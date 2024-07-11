# Function to calculate hash of content
from collections import Counter
import hashlib

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

def count_name_occurrences(trades):
    # Create a list of full names
    full_names = [f"{trade['firstName']} {trade['lastName']}" for trade in trades]
    
    # Count occurrences of each full name
    name_counts = Counter(full_names)
    
    # Convert Counter object to list of dictionaries
    result = [{'name': name, 'count': count} for name, count in name_counts.items()]
    
    return result
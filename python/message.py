from firebase_init import *
from firebase_admin import messaging
import json

def fetch_fcm_tokens():
    tokens = []

    # Fetch tokens from Firestore
    collection_ref = db.collection('fcm_tokens')
    docs = collection_ref.get()

    for doc in docs:
        tokens.append(doc.to_dict().get('token'))

    return tokens

def send_data_message(data):
    fcm_tokens = fetch_fcm_tokens()
    # Convert the trades_data array to a JSON string
    data_json = json.dumps(data)

    # Construct the data message payload
    data_message = messaging.MulticastMessage(
        data={
            'data': data_json
        },
        tokens=fcm_tokens
    )

    # Send the message
    response = messaging.send_multicast(data_message)
    print(f'Successfully sent message: {response.success_count} messages were sent successfully')
    
    if response.failure_count > 0:
        responses = response.responses
        for idx, resp in enumerate(responses):
            if not resp.success:
                # The token is invalid, log the token and error message
                print(f'Token {fcm_tokens[idx]} failed: {resp.exception}')
                delete_fcm_token(fcm_tokens[idx])


# Function to delete documents based on the token field
def delete_fcm_token(token_value):
    try:
        # Query the collection to find documents where the token field matches the specified value
        query = db.collection('fcm_tokens').where('token', '==', token_value)
        results = query.get()

        # Check if any documents were found
        if not results:
            print(f'No documents found with token: {token_value}')
            return

        # Delete each document that matches the query
        for doc in results:
            doc.reference.delete()
            print(f'Deleted document with ID: {doc.id}')

    except Exception as e:
        print(f'An error occurred: {e}')
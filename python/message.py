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
        failed_tokens = []
        for idx, resp in enumerate(responses):
            if not resp.success:
                # The token is invalid, log the token and error message
                failed_tokens.append(fcm_tokens[idx])
                print(f'Token {fcm_tokens[idx]} failed: {resp.exception}')
        remove_invalid_tokens(failed_tokens)

def remove_invalid_tokens(tokens):
    for token in tokens:
        db.collection('device_tokens').document(token).delete()
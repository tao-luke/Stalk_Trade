import firebase_admin
from firebase_admin import credentials, firestore
import os

current_dir = os.path.dirname(os.path.realpath(__file__))
json_file = 'stalk-db-firebase-adminsdk-g64um-cd9cc7e2a2.json'
json_path = os.path.join(current_dir, json_file)

# Initialize Firebase Admin SDK with the service account key
cred = credentials.Certificate(json_path)
firebase_admin.initialize_app(cred)

db = firestore.client()
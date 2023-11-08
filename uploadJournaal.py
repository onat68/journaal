import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from firebase_admin import db

cred = credentials.Certificate("journaal-8726c-firebase-adminsdk-7juo8-53044a14de.json")
app = firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://journaal-8726c-default-rtdb.europe-west1.firebasedatabase.app/'
})

entries_ref = db.reference('entries')
print(entries_ref.get())


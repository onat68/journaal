import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

cred = credentials.Certificate("journaal-8726c-firebase-adminsdk-7juo8-53044a14de.json")
app = firebase_admin.initialize_app(cred, name= 'Journaal')
db = firestore.client()
entries_ref = db.collection('entries')
docs = entries_ref.stream()

for doc in docs:
    print(doc)


# ref = db.reference('https://journaal-8726c-default-rtdb.europe-west1.firebasedatabase.app')
# ref = db.child('entries')

# result = firebase_admin.get_app('/entries', None)
# print(ref)

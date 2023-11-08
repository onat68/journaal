import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from firebase_admin import db
from Entry import Entry
import json
import docx2txt
import re

journal = docx2txt.process("journal.docx").split('*-*')
if len(journal) > 1:
    journal.pop(0)

docEntries = journal[0].split('--- ')
docEntries.pop(0)

entries = list()
for i in range(len(docEntries)):
    splitedEntry = re.split(r'Perso - |Pro -',docEntries[i])
    entry = Entry(splitedEntry[1],splitedEntry[2],splitedEntry[0]).dictify()
    entries.append(entry)


cred = credentials.Certificate("journaal-8726c-firebase-adminsdk-7juo8-53044a14de.json")
app = firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://journaal-8726c-default-rtdb.europe-west1.firebasedatabase.app/'
})
entries_ref = db.reference('entries')

entries_ref.set(entries)



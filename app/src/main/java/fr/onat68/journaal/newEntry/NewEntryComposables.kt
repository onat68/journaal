@file:OptIn(ExperimentalMaterial3Api::class)

package fr.onat68.journaal.newEntry

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import fr.onat68.journaal.EntryModel

@Composable
fun DateField(newEntryViewModel: NewEntryViewModel) {
    Text(text = newEntryViewModel.date.toString())
}

@Composable
fun NotePerso(
    perso: String,
    onNoteChange: (String, String) -> Unit,
) {
    TextField(
        value = perso,
        onValueChange = {onNoteChange(it, "perso")},
        readOnly = false,
        placeholder = { Text("Note personnelle") },
        modifier = Modifier.fillMaxWidth()
    )
}//

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotePro(
    pro: String,
    onNoteChange: (String, String) -> Unit,
    ) {
    TextField(
        value = pro,
        onValueChange = {onNoteChange(it, "pro")},
        readOnly = false,
        placeholder = { Text("Note professionnelle") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SendButton(
    sendEntry: (EntryModel) -> Unit,
    checkEntry: (EntryModel) -> Boolean,
    newEntry: EntryModel,
    navController: NavController
) {
    Button(onClick = {
        if(checkEntry(newEntry)){
            sendEntry(newEntry)
            navController.navigate("entries")
        }
        else {
            navController.navigate("error_newEntry")
        }

    }){
        Text("Envoyer")
    }
}

@Composable
fun ErrorEntry(navController: NavController) {
    fun onDismissRequest(navController: NavController){
        navController.navigate("newEntry")
    }
    AlertDialog(
        onDismissRequest = {
            onDismissRequest(navController)
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest(navController)
                }
            ) {
                Text("Annuler")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    navController.navigate("newEntry") // A voir si on retourne sur l'écran avec les données précédentes NavBackStackEntry
                }
            ) {
                Text("Valider")
            }
        },
        title = {
            Text(text = "Entrée déjà existante")
        },
        text = {
            Text(text = "Il y a déjà une entrée pour ce jour ci")
        })
}
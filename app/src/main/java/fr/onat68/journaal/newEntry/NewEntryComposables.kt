@file:OptIn(ExperimentalMaterial3Api::class)

package fr.onat68.journaal.newEntry

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.type.Date
import fr.onat68.journaal.EntriesRepository
import fr.onat68.journaal.EntryModel
import java.time.LocalDate
import java.util.Calendar

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
    newEntry: EntryModel,
    navController: NavController
) {
    Button(onClick = {
        sendEntry(newEntry)
        navController.navigate("entries")
    }){
        Text("Envoyer")
    }
}
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.type.Date
import fr.onat68.journaal.EntriesRepository
import java.time.LocalDate
import java.util.Calendar

@Composable
fun DateField(newEntryViewModel: NewEntryViewModel) {
    Text(text = newEntryViewModel.date.toString())
}

@Composable
fun NotePerso(newEntryViewModel: NewEntryViewModel) {
    var text by rememberSaveable { mutableStateOf(newEntryViewModel.newEntry.personnal) }
    TextField(
        value = text,
        onValueChange = { it ->
            newEntryViewModel.newEntry.personnal = it
            text = it
        },
        readOnly = false,
        placeholder = { Text("Note personnelle") },
        modifier = Modifier.fillMaxWidth()
    )
}


//

@Composable
fun NotePro(newEntryViewModel: NewEntryViewModel) {
    var text by rememberSaveable { mutableStateOf(newEntryViewModel.newEntry.professionnal) }
    TextField(
        value = text,
        onValueChange = { it ->
            newEntryViewModel.newEntry.professionnal = it
            text = it
        },
        readOnly = false,
        placeholder = { Text("Note professionnelle") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SendButton(repo: EntriesRepository, newEntryViewModel: NewEntryViewModel) {
    Button(onClick = {
        repo.add(newEntryViewModel.newEntry)
    }){
        Text("Envoyer")
    }
}
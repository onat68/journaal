package fr.onat68.journaal.newEntry

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import fr.onat68.journaal.EntriesRepository
import fr.onat68.journaal.EntryModel

@Composable
fun NewEntryScreen(repo: EntriesRepository, newEntryViewModel: NewEntryViewModel, navController: NavController) {
    val newEntry: State<EntryModel> = newEntryViewModel.newEntry.collectAsState(initial = EntryModel())
    Column {
        DateField(newEntryViewModel)
        NotePerso(newEntry.value.personnal, newEntryViewModel::onNoteChange)
        NotePro(newEntry.value.professionnal, newEntryViewModel::onNoteChange)
        SendButton(newEntryViewModel::sendEntry, newEntry.value, navController)
    }
}

//@Preview
//@Composable
//fun PreviewNewEntryScreen() {
//    NewEntryScreen(newEntryViewModel)
//}
package fr.onat68.journaal.newEntry

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import fr.onat68.journaal.EntriesRepository

@Composable
fun NewEntryScreen(repo: EntriesRepository, newEntryViewModel: NewEntryViewModel) {
    Column {
        DateField(newEntryViewModel)
        NotePerso(newEntryViewModel)
        NotePro(newEntryViewModel)
        SendButton(repo = repo, newEntryViewModel = newEntryViewModel )
    }
}

//@Preview
//@Composable
//fun PreviewNewEntryScreen() {
//    NewEntryScreen(newEntryViewModel)
//}
package fr.onat68.journaal.newEntry

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NewEntryScreen() {
    Column() {
        NotePro()
        NotePerso()
    }
}

@Preview
@Composable
fun PreviewNewEntryScreen() {
    NewEntryScreen()
}
@file:OptIn(ExperimentalMaterial3Api::class)

package fr.onat68.journaal.newEntry

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun NotePro() {
    var text by rememberSaveable { mutableStateOf("") }
    TextField(value = text, onValueChange = { text = it }, readOnly = false, placeholder = { Text("Note professionnelle")}, modifier = Modifier.fillMaxWidth())
}
@Composable
fun NotePerso() {
    var text by rememberSaveable { mutableStateOf("") }
    TextField(value = text, onValueChange = { text = it }, readOnly = false, placeholder = { Text("Note personnelle")}, modifier = Modifier.fillMaxWidth())
}
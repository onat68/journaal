package fr.onat68.journaal.readEntry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.onat68.journaal.EntryModel

@Composable
fun CardDetails(entry: EntryModel, hue: Float) {
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = Color.hsv(
                hue = hue,
                saturation = 0.08f,
                value = 1f
            )
        )
    ) {
        Column(
            modifier = Modifier
                .padding(all = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Note personnelle : " + entry.personnal)
            Spacer(modifier = Modifier.height(6.dp))
            Text("Note professionnelle : " + entry.professionnal)
        }
    }

}
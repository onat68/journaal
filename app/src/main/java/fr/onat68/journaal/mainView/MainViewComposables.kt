package fr.onat68.journaal.mainView

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import coil.compose.AsyncImage
import fr.onat68.journaal.EntryModel

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}

@Composable
fun LastEntries(navController: NavController) {
    Row(modifier = Modifier.height(50.dp)) {
        Text(
            text = "Dernières entrées dans le journal", modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
        ElevatedButton(onClick = { navController.navigate("addEntry") }) {
            Text("Ajouter une entrée")
        }
    }
}

@Composable
fun EntriesList(entriesList: SnapshotStateList<EntryModel?>, navController: NavController) {


    LazyColumn {
        itemsIndexed(
            entriesList
        ) { index, entry ->
            val hue: Float = (index * 240) / (entriesList.size.toFloat())
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                Modifier
                    .background(
                        Color.hsv(
                            hue = hue,
                            saturation = 0.2f,
                            value = 1f
                        )
                    )
                    .width(600.dp)
                    .clickable(onClick = { navController.navigate("details/${index}/${hue}") })
            ) {
                EntryCard(entry!!, index, navController)
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}

@Composable
fun EntryCard(entry: EntryModel, entryIndex: Int, navController: NavController) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        EntryImage(entry)
        Spacer(modifier = Modifier.width(6.dp))
        EntryText(entry)
    }
}

//@Preview
//@Composable
//fun PreviewEntryCard() {
//    EntryCard(EntryModel(), 1, navController = NavController(context))
//}

@Composable
fun EntryImage(entry: EntryModel) {

    AsyncImage(
        model = entry.imageUrl,
        contentDescription = "Temporary picture",
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(20.dp))
    )
}

@Composable
fun EntryText(entry: EntryModel) {
    Column(modifier = Modifier.padding(all = 8.dp)) {
        Text("${entry.day} ${entry.month} ${entry.year}")
        Spacer(modifier = Modifier.height(6.dp))
    }
}
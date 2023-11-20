package fr.onat68.journaal

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import fr.onat68.journaal.EntriesRepository.Singleton.entriesList


class MainActivity : ComponentActivity() {
    var context = this

    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo = EntriesRepository()
        repo.set()

        setContent {

            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "lastEntries") {
                navigation(
                    startDestination = "entries",
                    route = "lastEntries"
                ) {
                    composable("entries") {
                        Column {
                            LastEntries()
                            Spacer(modifier = Modifier.height(30.dp))
                            EntriesList(entriesList, navController)
                        }
                    }
                    composable(
                        "details/{entryIndex}/{entryHue}",
                        arguments = listOf(navArgument("entryIndex") {
                            type = NavType.IntType
                        }, navArgument("entryHue") { type = NavType.FloatType })
                    ) {
                        val entryIndex = it.arguments?.getInt("entryIndex") ?: -1
                        val entryHue = it.arguments?.getFloat("entryHue")
                        CardDetails(entriesList[entryIndex]!!, entryHue!!)
                    }
                }
            }
        }

    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}

@Composable
fun LastEntries() {
    Text(text = "Dernières entrées dans le journal",
        modifier = Modifier.wrapContentSize(Alignment.Center))
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






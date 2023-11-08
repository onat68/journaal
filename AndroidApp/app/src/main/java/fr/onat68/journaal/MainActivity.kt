package fr.onat68.journaal

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
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
import com.google.firebase.database.FirebaseDatabase
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


class MainActivity : ComponentActivity() {
    var context = this

    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val entriesList = mutableStateListOf<EntryModel?>()
        val client = OkHttpClient()

        @Serializable
        data class CatApiModel(val url: String)

        fun run(url: String, i: Int) {

            val request = Request.Builder()
                .url(url)
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val newUrl = Json {
                            ignoreUnknownKeys = true
                        }.decodeFromString<CatApiModel>(
                            response.body?.string().toString().removeSurrounding("[", "]")
                        ).url
                        entriesList[i] = entriesList[i]?.copy(imageUrl = newUrl)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })

        }

        val db = FirebaseDatabase.getInstance().getReference("entries")

        fun fetchData() {
            entriesList.clear()
            db.get().addOnSuccessListener { databaseSnapshot ->
                val sortedDB = databaseSnapshot.children.sortedWith(
                    compareBy({ (it as? EntryModel)?.year },
                        { (it as? EntryModel)?.month },
                        { (it as? EntryModel)?.day })
                ).reversed()
                for (e in sortedDB) {
                    val entry = e.getValue(EntryModel::class.java)
                    entriesList.add(entry)
                    run(
                        "https://api.thecatapi.com/v1/images/search?api_key=live_KRAgyaK4kDT8bmL6CpwExbchFaVMDYSNiOCA1eHv2Te7kiFz5S8tikKabqj9H5NA",
                        entriesList.size - 1
                    )
                }
            }
        }
        fetchData()

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
                        "details/{entryIndex}",
                        arguments = listOf(navArgument("entryIndex") {
                            type = NavType.IntType
                        })
                    ) {
                        var entryIndex = it.arguments?.getInt("entryIndex") ?: -1
                        CardDetails(entriesList[entryIndex]!!)
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
    Text(text = "Dernières entrées dans le journal")
}

@Composable
fun EntriesList(entriesList: SnapshotStateList<EntryModel?>, navController: NavController) {

    LazyColumn {
        itemsIndexed(
            entriesList
        ) { index, entry ->
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                Modifier
                    .background(Color.Gray)
                    .width(600.dp)
            ) {
                EntryCard(entry!!, index, navController)
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}

@Composable
fun EntryCard(entry: EntryModel, entryIndex: Int, navController: NavController) {
    Button(onClick = { navController.navigate("details/${entryIndex}") }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            EntryImage(entry)
            Spacer(modifier = Modifier.width(5.dp))
            EntryText(entry)
        }
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
fun CardDetails(entry: EntryModel){
    Column(modifier = Modifier.padding(all = 8.dp)) {
        Text("Note personnelle : " + entry.personnal)
        Spacer(modifier = Modifier.height(6.dp))
        Text("Note professionnelle : " + entry.professionnal)
    }
}






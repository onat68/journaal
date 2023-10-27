package fr.onat68.journaal

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import kotlinx.serialization.encodeToString


class MainActivity : ComponentActivity() {
    val context = this

    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var entriesList = mutableStateListOf<EntryModel?>()

        @Serializable
        data class CatApiModel(var url: String)

        fun run(url: String, e: Int, entry: EntryModel) {
            var newEntry = entry

            val request = Request.Builder()
                .url(url)
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    try{
                        newEntry.imageUrl = Json {
                            ignoreUnknownKeys = true
                        }.decodeFromString<CatApiModel>(
                            response.body?.string().toString().removeSurrounding("[", "]")
                        ).url
                        entriesList[e] = newEntry
                    }
                    catch(e: Exception) {
                        e.printStackTrace()
                        Log.d(TAG, "ICI")
                    }
                }
            })

        }


        setContent {


            val db = FirebaseDatabase.getInstance().getReference("entries")


            db.get().addOnSuccessListener { databaseSnapshot ->
                var count = 0
                for (e in databaseSnapshot.children) {
                    val entry = e.getValue(EntryModel::class.java)
                    run(
                        "https://api.thecatapi.com/v1/images/search?api_key=live_KRAgyaK4kDT8bmL6CpwExbchFaVMDYSNiOCA1eHv2Te7kiFz5S8tikKabqj9H5NA",
                        count,
                        entry!!
                    )

                    entriesList.add(entry)
                    count += 1
                }
                entriesList.sortWith(compareBy({ it?.year }, { it?.month }, { it?.day }))
                entriesList.reverse()
            }
            Column {
                LastEntries()
                Spacer(modifier = Modifier.height(30.dp))
                EntriesList(context, entriesList)
            }
        }

    }
}

private val client = OkHttpClient()

@Composable
fun LastEntries() {
    Text(text = "Dernières entrées dans le journal")
}


@Composable
fun EntriesList(context: Context, entriesList: MutableList<EntryModel?>) {

    LazyColumn {
        items(entriesList) { entry ->
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                Modifier
                    .background(Color.Gray)
                    .width(600.dp)
            ) {
                EntryCard(entry!!)
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}

@Composable
fun EntryCard(entry: EntryModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        EntryImage(entry)
        Spacer(modifier = Modifier.width(5.dp))
        EntryText(entry)
    }
}

@Preview
@Composable
fun PreviewEntryCard() {
    EntryCard(EntryModel())
}

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






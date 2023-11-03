package fr.onat68.journaal

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class SampleViewModel: ViewModel() {
    val entriesList = mutableStateListOf<EntryModel?>()
    val client = OkHttpClient()
    @Serializable
    data class CatApiModel(val url: String)

    fun run(url: String, i: Int, entry: EntryModel) {
        val newEntry = entry

        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                try {
                    newEntry.imageUrl = Json {
                        ignoreUnknownKeys = true
                    }.decodeFromString<CatApiModel>(
                        response.body?.string().toString().removeSurrounding("[", "]")
                    ).url
                    entriesList[i] = newEntry
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d(ContentValues.TAG, "ICI")
                }
            }
        })

    }

    val db = FirebaseDatabase.getInstance().getReference("entries")

    fun fetchData(){
        entriesList.clear()
        db.get().addOnSuccessListener { databaseSnapshot ->
            for (e in databaseSnapshot.children) {
                val entry = e.getValue(EntryModel::class.java)
                entriesList.add(entry)
                run(
                    "https://api.thecatapi.com/v1/images/search?api_key=live_KRAgyaK4kDT8bmL6CpwExbchFaVMDYSNiOCA1eHv2Te7kiFz5S8tikKabqj9H5NA",
                    entriesList.size - 1,
                    entry!!
                )
            }
            entriesList.sortWith(compareBy({ it?.year }, { it?.month }, { it?.day }))
            entriesList.reverse()
        }
    }

}
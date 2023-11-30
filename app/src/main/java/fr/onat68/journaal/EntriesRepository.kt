@file:Suppress("JSON_FORMAT_REDUNDANT")

package fr.onat68.journaal

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.database.FirebaseDatabase
import fr.onat68.journaal.EntriesRepository.Singleton.databaseRef
import fr.onat68.journaal.EntriesRepository.Singleton.entriesList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class EntriesRepository {

    object Singleton {
        val databaseRef = FirebaseDatabase.getInstance().getReference("entries")
        val entriesList = mutableStateListOf<EntryModel>()
        val _isLoading = MutableStateFlow(false)
        val isLoading = _isLoading.asStateFlow()
    }

    fun add(entry: EntryModel) {
        databaseRef.child(entry.id).setValue(entry)
    }

    fun delete(entryId: String) {
        databaseRef.child(entryId).removeValue()
    }

    fun set() {
        Singleton._isLoading.value = true
        entriesList.clear()
        databaseRef.get().addOnSuccessListener { databaseSnapshot ->

            val sortedDB = databaseSnapshot.children.map { it.getValue(EntryModel::class.java) }
                .filterNotNull()
                .sortedWith(compareBy({ it.year }, { it.month }, { it.day }))
                .reversed()

            for (entry in sortedDB) {
                entriesList.add(entry)
                getCatImage(
                    entriesList.size - 1
                )
            }
        }
        Singleton._isLoading.value = false
    }
}

val client = OkHttpClient()

@Serializable
private data class CatApiModel(val url: String)

private fun getCatImage(i: Int) {

    val url = "https://api.thecatapi.com/v1/images/search?api_key=live_KRAgyaK4kDT8bmL6CpwExbchFaVMDYSNiOCA1eHv2Te7kiFz5S8tikKabqj9H5NA"
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
                entriesList[i] = entriesList[i].copy(imageUrl = newUrl)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    })

}
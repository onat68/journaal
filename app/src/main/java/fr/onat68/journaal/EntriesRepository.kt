package fr.onat68.journaal

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.onat68.journaal.EntriesRepository.Singleton.databaseRef
import fr.onat68.journaal.EntriesRepository.Singleton.entriesList
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
        //        private val BUCKET_URL: String = "gs://journaal-8726c.appspot.com"
        //        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)
        val databaseRef = FirebaseDatabase.getInstance().getReference("entries")
        val entriesList = mutableStateListOf<EntryModel?>()
    }

    fun get() {
        entriesList.clear()
        databaseRef.get().addOnSuccessListener { databaseSnapshot ->
                val sortedDB = databaseSnapshot.children.sortedWith(
                    compareBy({ (it as? EntryModel)?.year },
                        { (it as? EntryModel)?.month },
                        { (it as? EntryModel)?.day })
                ).reversed()
                for (e in sortedDB) {
                    val entry = e.getValue(EntryModel::class.java)
                    entriesList.add(entry)
                    getCatImage(
                        "https://api.thecatapi.com/v1/images/search?api_key=live_KRAgyaK4kDT8bmL6CpwExbchFaVMDYSNiOCA1eHv2Te7kiFz5S8tikKabqj9H5NA",
                        entriesList.size - 1
                    )
                }
            Log.d(TAG, "ICI" + entriesList[0])
            }
    }

    val client = OkHttpClient()

    @Serializable
    private data class CatApiModel(val url: String)

    private fun getCatImage(url: String, i: Int) {

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


}
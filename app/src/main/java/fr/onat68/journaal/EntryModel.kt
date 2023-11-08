package fr.onat68.journaal

import kotlinx.serialization.Serializable

data class EntryModel(
    val id: String = "firstEntry",
    val day: Int = 20,
    val month: Int = 10,
    val year: Int = 2023,
    val personnal: String = "Hey",
    val professionnal: String = "Hoy",
    val imageUrl: String = "https://http.cat/images/400.jpg"
)

//data class EntryModel(
//    val id: String,
//    val day: Int,
//    val month: Int,
//    val year: Int,
//    val personnal: String,
//    val professionnal: String,
//    var imageUrl: String = "https://http.cat/images/400.jpg"
//)
package fr.onat68.journaal

import java.text.SimpleDateFormat
import java.util.Date

class EntryModel(
    var id: String = "firstEntry",
    var day: Int = 20,
    var month: Int = 10,
    var year: Int = 2023,
    var personnal: String = "Hey",
    var professionnal: String = "Hoy",
    var imageUrl: String = "https://http.cat/images/400.jpg"
)
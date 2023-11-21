package fr.onat68.journaal.newEntry

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import fr.onat68.journaal.EntryModel
import java.time.LocalDate
import java.util.UUID

@SuppressLint("NewApi")
class NewEntryViewModel() {

    val date = LocalDate.now()
    var newEntry = EntryModel(
        id = UUID.randomUUID().toString(),
        day = date.dayOfMonth,
        month = date.monthValue,
        year = date.year,
        personnal = "",
        professionnal = ""
    )

}
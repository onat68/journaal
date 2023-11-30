package fr.onat68.journaal.newEntry

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import fr.onat68.journaal.EntriesRepository
import fr.onat68.journaal.EntryModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import java.util.UUID

@SuppressLint("NewApi")
class NewEntryViewModel : ViewModel() {
    private val repo = EntriesRepository()
    val date: LocalDate = LocalDate.now()

    private val _newEntry = MutableStateFlow(
        EntryModel(
            id = UUID.randomUUID().toString(),
            day = date.dayOfMonth,
            month = date.monthValue,
            year = date.year
        )
    )
    val newEntry: Flow<EntryModel> = _newEntry

    fun checkEntry(entry: EntryModel): Boolean {
        return EntriesRepository.Singleton.entriesList.none { it.day == entry.day && it.month == entry.month && it.year == entry.year }
    }
    fun sendEntry(entry: EntryModel) {
        repo.add(entry)
        repo.set()
    }

    fun onNoteChange(note: String, type: String) {
        when (type) {
            "perso" -> _newEntry.value = _newEntry.value.copy(personnal = note)
            "pro" -> _newEntry.value = _newEntry.value.copy(professionnal = note)
        }
    }

}
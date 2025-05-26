package uk.ac.aber.dcs.cs31620.resitapp.model

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.resitapp.R
import uk.ac.aber.dcs.cs31620.resitapp.datasource.ResitAppRepository
import java.time.LocalDateTime

const val NUM_DAYS_RECENT: Long = 30

class ReadingsViewModel(application: Application):
    AndroidViewModel(application){
        private val repository: ResitAppRepository = ResitAppRepository(application)

        var recentReadings: LiveData<List<Reading>> = loadRecentReadings()
        private set

        var readingList: LiveData<List<Reading>> = repository.getAllReadings()
        private set

    //TODO: May need to add specific index value to resource array
    private val anyName = application.resources.getStringArray(R.array.name_array)[0]
    private val anyKwh = application.resources.getStringArray(R.array.kwh_array)[0]
    private val anyDate = application.resources.getStringArray(R.array.date_array)[0]

    var readingSearch: ReadingSearch by mutableStateOf(
        ReadingSearch(
            name = anyName,
            kwh = anyKwh,
            date = anyDate
        )
    )
        private set

    fun updateReadingSearch(value: ReadingSearch) {
        getReadings(value)
    }

    fun insertReading(newReading: Reading) {
        viewModelScope.launch(Dispatchers.IO){
            repository.insert(newReading)
        }
    }

    fun updateReading(newReading: Reading) {
        viewModelScope.launch(Dispatchers.IO){
            repository.update(newReading)
        }
    }

    fun deleteReading(reading: Reading) {
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteReading(reading)
        }
    }

    private fun loadRecentReadings(): LiveData<List<Reading>> {

        val endDate = LocalDateTime.now().plusDays(365)
        val pastDate = LocalDateTime.now().minusDays(30)

        return repository.getRecentReadings(pastDate,endDate)
    }

    private fun getReadings(newReadingSearch: ReadingSearch) {
        val startDate: LocalDateTime
        val endDate: LocalDateTime
        var changed = false

        if (newReadingSearch.name != readingSearch.name) {
            changed = true
        }

        if (newReadingSearch.kwh != readingSearch.kwh) {
            changed = true
        }

        if (newReadingSearch.date != readingSearch.date) {
            changed = true
        }

        //TODO: Results may be restricted by these conditions
        if (changed) {
            } else if (newReadingSearch.name == anyName && newReadingSearch.kwh != anyKwh && newReadingSearch.date == anyDate) {
                //TODO: May be broken
                readingList = repository.getReadingsByKwh(newReadingSearch.kwh.toInt())
            } else if (newReadingSearch.name == anyName && newReadingSearch.kwh == anyKwh && newReadingSearch.date != anyDate) {
                //startDate = getStartDate
                //TODO: TO FINISH
            }
            readingSearch = newReadingSearch
        }
    }

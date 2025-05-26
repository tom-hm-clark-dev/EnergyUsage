package uk.ac.aber.dcs.cs31620.resitapp.datasource

import android.app.Application
import uk.ac.aber.dcs.cs31620.resitapp.model.Reading
import java.time.LocalDateTime

class ResitAppRepository(application: Application) {
    private val readingDao = ResitAppRoomDatabase.getDatabase(application)!!.readingDao()

    fun insert(reading: Reading) {
        readingDao.insertSingleReading(reading)
    }

    fun update(reading: Reading) {
        readingDao.updateReading(reading)
    }

    fun deleteReading(reading: Reading) {
        readingDao.deleteReading(reading)
    }

    fun insertMultipleReadings(readings: List<Reading>) {
        readingDao.insertMultipleReadings(readings)
    }

    fun getAllReadings() =
        readingDao.getAllReadings()

    fun getReadings(totalKwh: Int, kwh: Int, startDate: LocalDateTime, endDate: LocalDateTime) =
        readingDao.getReadings(totalKwh, kwh, startDate, endDate)

    fun getRecentReadings(startDate: LocalDateTime, endDate: LocalDateTime) =
        readingDao.getReadingsAdmittedBetweenDates(startDate, endDate)

    fun getRecentReadingsSync(startDate: LocalDateTime, endDate: LocalDateTime) =
        readingDao.getReadingsAdmittedBetweenDatesSync(startDate, endDate)

    fun getReadingsByTotalKwh(totalKwh: Int) = readingDao.getReadingsByTotalKwh(totalKwh)

    fun getReadingsByKwh(kwh: Int) = readingDao.getReadingsByKwh(kwh)

    fun getReadingsAddedBetweenDates(startDate: LocalDateTime, endDate: LocalDateTime) =
        readingDao.getReadingsAddedBetweenDates(startDate, endDate)

    fun getReadingsByTotalKwhAndKwh(totalKwh: Int, kwh: Int) = readingDao.getReadingsByTotalKwhAndKwh(totalKwh, kwh)

    fun getReadingsByTotalKwhAndAddedBetweenDates(totalKwh: Int, startDate: LocalDateTime, endDate: LocalDateTime) =
        readingDao.getReadingsByTotalKwhAndAddedBetweenDates(totalKwh, startDate, endDate)

    fun getReadingsByKwhAndAddedBetweenDates(kwh: Int, startDate: LocalDateTime, endDate: LocalDateTime) =
        readingDao.getReadingsByKwhAndAddedBetweenDates(kwh, startDate, endDate)


    }

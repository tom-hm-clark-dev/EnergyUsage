package uk.ac.aber.dcs.cs31620.resitapp.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import java.time.LocalDateTime

@Dao
interface ReadingDao {
    @Insert
    fun insertSingleReading(reading: Reading)

    @Insert
    fun insertMultipleReadings(readingsList: List<Reading>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateReading(reading: Reading)

    @Delete
    fun deleteReading(reading: Reading)

    @Query("DELETE FROM readings")
    fun deleteAll()

    @Query("SELECT * FROM readings")
    fun getAllReadings(): LiveData<List<Reading>>

  @Query("""SELECT * FROM readings WHERE kwh =:kwh AND totalKwh =:totalKwh AND date BETWEEN :startDate AND :endDate""")
    fun getReadings(kwh: Int, totalKwh: Int, startDate: LocalDateTime, endDate: LocalDateTime): LiveData<List<Reading>>

    @Query("""SELECT * FROM readings WHERE admission_date BETWEEN :startDate AND :endDate ORDER BY :startDate DESC""")
    fun getReadingsAdmittedBetweenDates(startDate: LocalDateTime, endDate: LocalDateTime): LiveData<List<Reading>>

    @Query("""SELECT * FROM readings WHERE admission_date BETWEEN :startDate AND :endDate""")
    fun getReadingsAdmittedBetweenDatesSync(startDate: LocalDateTime, endDate: LocalDateTime): List<Reading>

    @Query("SELECT * FROM readings WHERE totalKwh = :totalKwh")
    fun getReadingsByTotalKwh(totalKwh: Int): LiveData<List<Reading>>

    @Query("SELECT * FROM readings WHERE kwh = :kwh")
    fun getReadingsByKwh(kwh: Int): LiveData<List<Reading>>

    @Query("SELECT * FROM readings WHERE date BETWEEN :startDate AND :endDate")
    fun getReadingsAddedBetweenDates( startDate: LocalDateTime, endDate: LocalDateTime): LiveData<List<Reading>>

    @Query("SELECT * FROM readings WHERE totalKwh = :totalKwh AND kwh = :kwh")
    fun getReadingsByTotalKwhAndKwh(totalKwh: Int, kwh: Int): LiveData<List<Reading>>

    @Query("""SELECT * FROM readings WHERE totalKwh = :totalKwh AND date BETWEEN :startDate AND :endDate""")
    fun getReadingsByTotalKwhAndAddedBetweenDates( totalKwh: Int, startDate: LocalDateTime, endDate: LocalDateTime): LiveData<List<Reading>>

    @Query("""SELECT * FROM readings WHERE kwh = :kwh AND date BETWEEN :startDate AND :endDate""")
    fun getReadingsByKwhAndAddedBetweenDates( kwh:Int, startDate: LocalDateTime, endDate: LocalDateTime): LiveData<List<Reading>>
}
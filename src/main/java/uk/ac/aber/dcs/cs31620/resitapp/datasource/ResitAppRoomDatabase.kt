package uk.ac.aber.dcs.cs31620.resitapp.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.resitapp.datasource.util.LocalDateTimeConverter
import uk.ac.aber.dcs.cs31620.resitapp.model.Reading
import uk.ac.aber.dcs.cs31620.resitapp.model.ReadingDao
import java.time.LocalDateTime

@Database(entities = [Reading::class], version = 1)
//TODO: May be broken
@TypeConverters(LocalDateTimeConverter::class)
abstract class ResitAppRoomDatabase : RoomDatabase() {

    abstract fun readingDao(): ReadingDao
    companion object {
        private var instance: ResitAppRoomDatabase? = null
        private val coroutineScope = CoroutineScope(Dispatchers.IO)

        @Synchronized
        fun getDatabase(context: Context): ResitAppRoomDatabase? {
            if(instance == null){
                instance = Room.databaseBuilder<ResitAppRoomDatabase>(
                    context.applicationContext,
                    ResitAppRoomDatabase::class.java,
                    "resitapp_database"
                )
                    .allowMainThreadQueries()
                    // addCallback is "optional"
                    .addCallback(roomDatabaseCallback(context))
                    .build()
            }
            return instance
        }

        private fun roomDatabaseCallback(context: Context): Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    coroutineScope.launch {
                        //populateDatabase(context, getDatabase(context)!!)
                    }
                }
            }
        }

        /* private fun populateDatabase(context: Context, instance: ResitAppRoomDatabase) {
            val upToOneYear = LocalDateTime.now().minusDays(362/2)
            val from1to2Years = LocalDateTime.now().minusDays(365 + (36/2))
            val june = LocalDateTime.now().minusDays(30)
            val april = LocalDateTime.now().minusDays(90)
            val march = LocalDateTime.now().minusDays(120)
            val veryRecentAdmission = LocalDateTime.now()

            val upToOneYearReading = Reading(
                0, 0, 84, .32f, june, veryRecentAdmission
            )
            val from1to2YearsReading = Reading(
                0, 0, 90, .42f, june, veryRecentAdmission
            )
            val testReadingNo3 = Reading(
                0, 0, 97, .46f, april, veryRecentAdmission
            )

            val testReadingNo4 = Reading(
                0, 0, 100, .51f, march, veryRecentAdmission
            )

            val testReadingNo5 = Reading(
                0, 0, 102, .42f, june, veryRecentAdmission
            )
            val testReadingNo6 = Reading(
                0, 0, 4, .46f, april, veryRecentAdmission
            )

            val testReadingNo7 = Reading(
                0, 0, 12, .51f, march, veryRecentAdmission
            )
            // Change gradle API to minimum ver 26 if doesn't work

            val readingList = mutableListOf(
                upToOneYearReading,
                from1to2YearsReading,
                testReadingNo3,
                testReadingNo4,
                testReadingNo5,
                testReadingNo6,
                testReadingNo7
            )

            val dao = instance.readingDao()
            dao.insertMultipleReadings(readingList)
        } */
    }

}
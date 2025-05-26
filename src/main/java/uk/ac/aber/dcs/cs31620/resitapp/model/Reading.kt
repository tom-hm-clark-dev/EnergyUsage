package uk.ac.aber.dcs.cs31620.resitapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Entity(tableName = "readings")
data class Reading(
    //TODO: CHANGE VALs TO FLOATS
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var totalKwh: Int = 0,
    var kwh: Int = 0,
    var pricePerKwh: Float = 0.0f,
    var date: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "admission_date")
    var admissionDate: LocalDateTime = LocalDateTime.now()
)
{

}
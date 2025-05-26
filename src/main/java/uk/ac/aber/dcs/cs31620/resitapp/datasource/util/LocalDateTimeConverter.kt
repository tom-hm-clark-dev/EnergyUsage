package uk.ac.aber.dcs.cs31620.resitapp.datasource.util

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.TimeZone

object LocalDateTimeConverter {

    @TypeConverter
    @JvmStatic
    fun toLocalDate(timestamp: Long): LocalDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(timestamp),
        TimeZone.getDefault().toZoneId()
    )

    @TypeConverter
    @JvmStatic
    fun toTimestamp(localDateTime: LocalDateTime): Long =
        localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}
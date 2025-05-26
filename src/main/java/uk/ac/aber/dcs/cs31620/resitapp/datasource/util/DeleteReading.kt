package uk.ac.aber.dcs.cs31620.resitapp.datasource.util

import uk.ac.aber.dcs.cs31620.resitapp.model.Reading

fun deleteReading(
    reading: Reading,
    doDelete: (Reading) -> Unit = {}
) {
    doDelete(reading)
}
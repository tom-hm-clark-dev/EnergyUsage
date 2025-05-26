package uk.ac.aber.dcs.cs31620.resitapp.ui.components

//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import uk.ac.aber.dcs.cs31620.resitapp.R
import uk.ac.aber.dcs.cs31620.resitapp.datasource.util.deleteReading
import uk.ac.aber.dcs.cs31620.resitapp.model.Reading
//import uk.ac.aber.dcs.cs31620.resitapp.ui.readings.deleteReading
//import uk.ac.aber.dcs.cs31620.resitapp.model.readings
import uk.ac.aber.dcs.cs31620.resitapp.ui.theme.ResitAppTheme
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun ReadingCard(
    reading: Reading,
    modifier: Modifier = Modifier,
    totalKwh: Int,
    selectAction: (Reading) -> Unit = {},
    deleteAction: (Reading) -> Unit = {},
) {

    // Formats the price per kwh float to stop at 2 decimal points
    val pPKwhReading = (reading.kwh * reading.pricePerKwh)
    val formatedPPKwhReading = String.format("%.2f", pPKwhReading)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        ConstraintLayout {
            //CalculateTotalKwh(readingsList)
            val (dateRef, kwhRef, ppkwhRef, costRef, deleteRef, totalKwhRef) = createRefs()
            val currentKwhToDate: Int
            Text(
                text = DateTimeFormatter
                    .ofPattern("dd MMM yyyy")
                    .format(reading.date),
                fontSize = 28.sp,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .constrainAs(dateRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.bottom)
                        bottom.linkTo(parent.bottom)
                    }
            )
            Text(
                text = "kWh: " + reading.kwh.toString().padStart(5, '0'),
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 8.dp, top = 32.dp)
                    .constrainAs(kwhRef) {
                        start.linkTo(dateRef.start)
                        top.linkTo(dateRef.bottom, margin = 6.dp)
                        bottom.linkTo(parent.bottom)
                    }
            )

            Text(
                text = "£ per kWh: " + reading.pricePerKwh.toString(),
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 8.dp, top = 32.dp)
                    .constrainAs(ppkwhRef) {
                        start.linkTo(kwhRef.end)
                        top.linkTo(dateRef.bottom, margin = 6.dp)
                        bottom.linkTo(parent.bottom)
                    }
            )

            Text(
                text = "£ spent: " + formatedPPKwhReading,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 8.dp, top = 32.dp)
                    .constrainAs(costRef) {
                        start.linkTo(ppkwhRef.start, margin = 100.dp)
                        top.linkTo(dateRef.bottom, margin = 6.dp)
                        bottom.linkTo(parent.bottom)
                    }
            )

            Text(
                text = ( totalKwh.toString().padStart(5, '0') + "kWh to date"),
                fontSize = 32.sp,
                modifier = Modifier
                    .padding(start = 8.dp, top = 16.dp)
                    .constrainAs(totalKwhRef) {
                        start.linkTo(dateRef.start)
                        top.linkTo(kwhRef.bottom, margin = 85.dp)
                        bottom.linkTo(parent.bottom)
                    }
            )

            IconButton(
                onClick = {
                    deleteAction(reading)
                },
                modifier = Modifier
                    .constrainAs(deleteRef) {
                    end.linkTo(parent.end)
                    start.linkTo(dateRef.end, margin = 190.dp)
                    top.linkTo(dateRef.top, margin = 24.dp)
                    bottom.linkTo(costRef.top)
                }
            ) {

                Icon(
                    imageVector = Icons.Filled.Delete,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = stringResource(R.string.remove_reading)
                )
            }
        }
    }
}

fun CalculateTotalKwh(readingsList: List<Reading>) {
    var currentKwhToDate: Int
    for (readingItem in readingsList) {
        currentKwhToDate = readingItem.kwh + readingsList.last().totalKwh
    }
}


@Preview
@Composable
private fun ReadingCardPreview(){
    ResitAppTheme(
        dynamicColor = false) {
       /*ReadingCard(reading = Reading(
           totalKwh = 98890,
           kwh = 7,
           pricePerKwh = 0.2f,
           date = LocalDateTime.now()
       )
       )
   */ }
}
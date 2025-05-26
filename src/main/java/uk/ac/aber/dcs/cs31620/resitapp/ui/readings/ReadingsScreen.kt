package uk.ac.aber.dcs.cs31620.resitapp.ui.readings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.resitapp.R
import uk.ac.aber.dcs.cs31620.resitapp.ui.components.ReadingCard
import uk.ac.aber.dcs.cs31620.resitapp.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.resitapp.ui.theme.ResitAppTheme
import uk.ac.aber.dcs.cs31620.resitapp.model.Reading
import uk.ac.aber.dcs.cs31620.resitapp.model.ReadingSearch
import uk.ac.aber.dcs.cs31620.resitapp.model.ReadingsViewModel
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import uk.ac.aber.dcs.cs31620.resitapp.ui.components.CurrentPricePerKWhArea
import uk.ac.aber.dcs.cs31620.resitapp.ui.components.DefaultSnackbar
import uk.ac.aber.dcs.cs31620.resitapp.ui.components.SearchArea
import uk.ac.aber.dcs.cs31620.resitapp.ui.navigation.Screen
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

const val MAX_READING_KWH = 99999

@Composable
fun ReadingsScreenTopLevel(
    navController: NavController,
    readingsViewModel: ReadingsViewModel = viewModel()
) {
    val readingList by readingsViewModel.readingList.observeAsState(listOf())
    val readingsViewModel: ReadingsViewModel = viewModel() // Obtain the ViewModel

    ReadingsScreen(
        readingsList = readingList,
        readingsViewModel = readingsViewModel,
        readingSearch = readingsViewModel.readingSearch,

        updateSearchCriteria = { readingSearch ->
            readingsViewModel.updateReadingSearch(readingSearch)
        },
        navController = navController
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReadingsScreen(
    readingsList: List<Reading> = listOf(),
    readingsViewModel: ReadingsViewModel,
    readingSearch: ReadingSearch = ReadingSearch(),
    updateSearchCriteria: (ReadingSearch) -> Unit = {},
    navController: NavController
    )
{
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState()}

    var startDate by rememberSaveable { mutableStateOf<LocalDate?>(null) }
    var endDate by rememberSaveable { mutableStateOf<LocalDate?>(null) }
    val context = LocalContext.current

    val startDateDialogState = rememberMaterialDialogState()
    val endDateDialogState = rememberMaterialDialogState()

    TopLevelScaffold(
        navController = navController,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddReading.route)
                /*    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Add reading",
                            actionLabel = "Undo"
                        )
                    } */
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_reading)
                )
            }
        },        snackbarContent = { data ->
            DefaultSnackbar(
                data = data,
                modifier = Modifier.padding(bottom = 4.dp),
                onDismiss = {
                    data.dismiss()
                }
            )
        },
        coroutineScope = coroutineScope,
        snackbarHostState = snackbarHostState
    )
    {
            innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            Row (
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                //verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton (onClick = { startDateDialogState.show()}) {
                    Text(text = startDate
                        ?.format(DateTimeFormatter
                        .ofPattern("dd MMM yyyy"))
                        ?: "Filter readings from:")
                }
                Spacer(modifier = Modifier
                    .width(4.dp))

                OutlinedButton (onClick = {endDateDialogState.show()}) {
                    Text(text = endDate
                        ?.format(DateTimeFormatter
                            .ofPattern("dd MMM yyyy"))
                        ?: "Filter readings to:")
                }
            }

            MaterialDialog (
                dialogState = startDateDialogState,
                buttons = {
                    positiveButton(text = stringResource(R.string.ok))
                    negativeButton(text = stringResource(R.string.cancel))
                }
            )        {
                datepicker() {date ->
                    startDate = date
                }
            }

            MaterialDialog(
                dialogState = endDateDialogState,
                buttons = {
                    positiveButton(text = "Ok")
                    negativeButton(text = "Cancel")
                }
            ) {
                datepicker { date ->
                    endDate = date
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(bottom = 64.dp), // Add some space at the end so that FAB not hidden
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
            ) {

                // Filter readings based on dates, code made with help of chatGPT
                val filteredReadings = readingsList.filter { reading ->
                    val readingDate = reading.date.toLocalDate()
                    (startDate == null || !readingDate.isBefore(startDate)) &&
                            (endDate == null || !readingDate.isAfter(endDate))

                }.sortedBy { it.date }

                // Sort readings by date ascending for "kWh to date" calculations
                val sortedReadings = readingsList.sortedBy { it.date }

                // Calculates kWh used to date and resets the meter if the maximum reading is exceeded
                var kwhTotal = 0
                var readingsWithKwhTotalCalculated = filteredReadings.map {reading ->
                    kwhTotal += reading.kwh
                    if (kwhTotal > MAX_READING_KWH) {
                        kwhTotal -= (MAX_READING_KWH + 1)
                    }
                    Pair(reading,kwhTotal)
                }

                // Reverses the sorting for date descending display
                items(readingsWithKwhTotalCalculated.reversed()) {(reading, totalKwh) ->
                    ReadingCard(
                        reading = reading,
                        modifier = Modifier
                            .padding(end = 4.dp, top = 4.dp),
                        totalKwh = totalKwh,
                        deleteAction = {
                            selectedReading ->
                            readingsViewModel.deleteReading(selectedReading)
                        }
                    )
                }
            }

        }
    }
}

    @Preview(showBackground = true)
    @Composable
    fun ReadingsScreenPreview() {
        val navController = rememberNavController()
        ResitAppTheme(dynamicColor = false) {
            //ReadingsScreen(navController)
        }
    }


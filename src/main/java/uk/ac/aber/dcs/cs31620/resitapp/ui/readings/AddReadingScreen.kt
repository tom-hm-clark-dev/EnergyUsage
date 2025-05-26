package uk.ac.aber.dcs.cs31620.resitapp.ui.readings

import android.app.Activity
//import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import uk.ac.aber.dcs.cs31620.resitapp.R
import uk.ac.aber.dcs.cs31620.resitapp.datasource.util.deleteReading
import uk.ac.aber.dcs.cs31620.resitapp.model.Reading
import uk.ac.aber.dcs.cs31620.resitapp.model.ReadingsViewModel
import uk.ac.aber.dcs.cs31620.resitapp.ui.components.ButtonSpinner
import uk.ac.aber.dcs.cs31620.resitapp.ui.theme.ResitAppTheme
import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.material3.*

@Composable
fun AddReadingScreenTopLevel(
    navController: NavHostController,
    readingsViewModel: ReadingsViewModel = viewModel()
) {
    val readingList by readingsViewModel.readingList.observeAsState(listOf())

    AddReadingScreen(
        readingsList = readingList,
        navController = navController,
        insertReading = { newReading ->
            readingsViewModel.insertReading(newReading)
        },
        updateReading = { newReading ->
            readingsViewModel.updateReading(newReading)
        },
        deleteReading = { reading ->
            readingsViewModel.deleteReading(reading)
        },

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReadingScreen(
    readingsList: List<Reading> = listOf(),
    navController: NavHostController,
    insertReading: (Reading) -> Unit = {},
    updateReading: (Reading) -> Unit = {},
    deleteReading: (Reading) -> Unit = {}
) {
    var readingTotalKwh by rememberSaveable { mutableStateOf(0) }
    var readingKwh by rememberSaveable { mutableStateOf("") }
    var readingPpKwh by rememberSaveable { mutableStateOf("") }
    var readingDate by remember { mutableStateOf(LocalDate.now()) }
    var showDialog by remember { mutableStateOf(false) }

    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("dd MMM yyyy")
                .format(readingDate)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (readingKwh.isNotEmpty() && readingPpKwh.isNotEmpty()) {
                        showDialog = true
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(R.string.add_reading)
                )
            }
        },
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(stringResource(R.string.add_reading))
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.goBack)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            ReadingDate(
                formattedDate = formattedDate,
                modifier = Modifier
                    .padding(top = 24.dp),
                updateDate = {
                    readingDate = it
                }
            )

            KwhInput(
                readingKwh = readingKwh,
                modifier = Modifier
                    .padding(top = 8.dp, start = 24.dp, end = 24.dp),
                updateKwh = {
                    readingKwh = it
                }
            )

            PricePerKwhInput(
                readingPpKwh = readingPpKwh,
                modifier = Modifier
                    .padding(top = 8.dp, start = 24.dp, end = 24.dp),
                updatePpKwh = {
                    readingPpKwh = it
                }
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(onClick = {
                        showDialog = false

                        if (readingsList.isEmpty()) {
                            insertReading(
                                totalKwh = readingKwh.toInt(),
                                kwh = readingKwh.toInt(),
                                ppkwh = readingPpKwh.toFloat(),
                                date = readingDate,
                                doInsert = { newReading ->
                                    insertReading(newReading)
                                }
                            )
                        } else {
                            for (reading in readingsList) {
                                if (readingDate.toString() == reading.date.toLocalDate().toString()) {
                                    deleteReading(
                                        reading,
                                        doDelete = { selectedReading ->
                                            deleteReading(selectedReading)
                                        }
                                    )
                                }
                            }
                            insertReading(
                                totalKwh = ((readingsList.sortedByDescending { it.date }.last().totalKwh) + readingTotalKwh),
                                kwh = readingKwh.toInt(),
                                ppkwh = readingPpKwh.toFloat(),
                                date = readingDate,
                                doInsert = { newReading ->
                                    insertReading(newReading)
                                }
                            )
                        }
                        navController.navigateUp()
                    }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = {
                    Text("Confirm Action")
                },
                text = {
                    Text("Do you want to add this reading? \n" +
                            "Date: $readingDate \n" +
                            "kWh: $readingKwh \n" +
                            "£ per kWh: $readingPpKwh")
                }
            )
        }
    }
}

//TODO: Changed kwh to String, may need to be Int
private fun insertReading(
    totalKwh: Int,
    kwh: Int,
    ppkwh: Float,
    date: LocalDate,
    doInsert: (Reading) -> Unit = {}
) {
        val reading = Reading(
            id = 0,
            totalKwh = (totalKwh ),
            kwh = kwh,
            pricePerKwh = ppkwh,
            date = date.atStartOfDay(), // We don't care about the time
            admissionDate = LocalDateTime.now(),
        )
        doInsert(reading)

}

@Composable
fun ReadingDate(
    formattedDate: String,
    modifier: Modifier,
    updateDate: (LocalDate) -> Unit
) {
    // States used to show the dialog
    val dateDialogState = rememberMaterialDialogState()

    OutlinedButton(
        onClick = {
            dateDialogState.show()
        },
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.date, formattedDate),
            fontSize = 16.sp
        )
    }

    // Code based on YouTube video: https://bit.ly/3VWY77x
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = stringResource(R.string.ok))
            negativeButton(text = stringResource(R.string.cancel))
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = stringResource(R.string.pickDate)
        ) {
            updateDate(it)
        }

    }

}

@Composable
fun KwhInput(
    readingKwh: String,
    modifier: Modifier,
    updateKwh: (String) -> Unit
) {
    OutlinedTextField(
        value = readingKwh,
        label = {
            Text(text = stringResource(id = R.string.reading_kwh))
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        // Parses input to ignore non-digit inputs
        onValueChange = {
            parsedKwh ->
            if (parsedKwh.all { it.isDigit() }) {
                updateKwh(parsedKwh)
            }
                        },
        modifier = modifier
    )
}

@Composable
fun PricePerKwhInput(
    readingPpKwh: String,
    modifier: Modifier,
    updatePpKwh: (String) -> Unit
) {
    OutlinedTextField(
        value = readingPpKwh,
        label = {
            Text(text = (stringResource(id = R.string.reading_ppkwh)))
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        // Parses input to allow only decimal digits
        onValueChange = {
                newText ->
            if (newText.inputIsValid()) {
                updatePpKwh(newText)
            }},
        modifier = modifier
    )
}

// Parses the input to only allow integers and decimal points. Created with assistance of chatGPT
fun String.inputIsValid(): Boolean {
    return this.isNotEmpty() && this.matches(Regex("^\\d*\\.?\\d*\$"))
}

@Composable
private fun HandleBackButton(navController: NavHostController) {
    // When back button is pressed we will navigate up the Compose
    // hierarchy. navigateUp will pop the Compose navigation back stack automatically.
    val backCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                navController.navigateUp()
            }
        }

    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher) {
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        onDispose {
            backCallback.remove()
        }
    }

}

@Preview(showBackground = true)
@Composable
fun AddReadingsScreenPreview() {
    val navController = rememberNavController()
    ResitAppTheme(dynamicColor = false) {
      //  AddReadingScreen(navController)
    }
}




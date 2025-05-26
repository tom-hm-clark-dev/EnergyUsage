package uk.ac.aber.dcs.cs31620.resitapp.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.resitapp.R
import uk.ac.aber.dcs.cs31620.resitapp.datasource.ResitAppRepository
import uk.ac.aber.dcs.cs31620.resitapp.model.Reading
import uk.ac.aber.dcs.cs31620.resitapp.model.ReadingsViewModel
//import uk.ac.aber.dcs.cs31620.resitapp.model.readings
import uk.ac.aber.dcs.cs31620.resitapp.ui.components.MainPageNavigationBar
import uk.ac.aber.dcs.cs31620.resitapp.ui.components.MainPageTopAppBar
import uk.ac.aber.dcs.cs31620.resitapp.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.resitapp.ui.theme.ResitAppTheme
import java.time.LocalDateTime
import kotlin.random.Random



@Composable
fun HomeScreenTopLevel(
    navController: NavController,
    readingsViewModel: ReadingsViewModel = viewModel()
) {
    val recentReadings by readingsViewModel.recentReadings.observeAsState(listOf())
    val readingList by readingsViewModel.readingList.observeAsState(listOf())

    HomeScreen(
        navController = navController,
        recentReadings = recentReadings,
        readingsList = readingList,
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    recentReadings: List<Reading>,
    readingsList: List<Reading> = listOf(),
){
    val coroutineScope = rememberCoroutineScope()

    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope
    ) {
        innerPadding ->
        Surface (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            HomeScreenContent(
                modifier = Modifier.padding(8.dp),
                recentReadings,
                readingsList
            )
        }
    }
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    recentReadings: List<Reading>,
    readingsList: List<Reading> = listOf(),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.welcome),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.featured_reading_title),
            fontSize = 18.sp
        )

        if (readingsList.isEmpty()) {
            Text(text = "Create some readings to view your average kWh per day")
        }
        else {
            var totalKwh = 0
            var totalReadings = 0
            for (reading in readingsList) {
                totalKwh += reading.kwh
                totalReadings++

            }
            val averageKwhPerDayResult = (totalKwh / totalReadings)

            Text(text = "Your average daily kWh usage is: $averageKwhPerDayResult")
        }

        FeaturedReading(Modifier.fillMaxWidth(), recentReadings, readingsList)
    }
}


@Composable
private fun FeaturedReading(
    modifier: Modifier = Modifier,
    recentReadings: List<Reading>,
    readingsList: List<Reading> = listOf(),
    )
{
    if (recentReadings.isNotEmpty()) {
        val readingPos = Random.nextInt(recentReadings.size)
        val readingName = recentReadings[readingPos].totalKwh
        val readingDate = recentReadings[readingPos].date


    }

   /* val context = LocalContext.current.applicationContext
    LaunchedEffect(key1 = Unit) {
        val repository = ResitAppRepository(context as Application)
        val past = LocalDateTime.now().minusDays(30)
        repository.getRecentReadingsSync(past, LocalDateTime.now())
    } */

/*    val readingPos = Random.nextInt(readings.size)
    var readingslist = readings

    println(readings)
    Text(text = readingslist.toString()) */

}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    ResitAppTheme(dynamicColor = false) {
       // HomeScreen(navController, listOf())
    }
}
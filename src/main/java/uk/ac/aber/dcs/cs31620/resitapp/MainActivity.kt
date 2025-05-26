package uk.ac.aber.dcs.cs31620.resitapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.resitapp.model.ReadingsViewModel
import uk.ac.aber.dcs.cs31620.resitapp.ui.home.HomeScreen
import uk.ac.aber.dcs.cs31620.resitapp.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.resitapp.ui.readings.ReadingsScreen
import uk.ac.aber.dcs.cs31620.resitapp.ui.theme.ResitAppTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.aber.dcs.cs31620.resitapp.ui.home.HomeScreenTopLevel
import uk.ac.aber.dcs.cs31620.resitapp.ui.readings.AddReadingScreenTopLevel
import uk.ac.aber.dcs.cs31620.resitapp.ui.readings.ReadingsScreenTopLevel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ResitAppTheme(dynamicColor = false) {
                BuildNavigationGraph()
            }
        }
    }
}

//TODO: Search kind of works, just finished workshop 18 section 1

@Composable
private fun BuildNavigationGraph(
    readingsViewModel: ReadingsViewModel = viewModel()
) {
    val currentPricePerKwh: Int = 12
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreenTopLevel(navController, readingsViewModel)
        }
        composable(Screen.Readings.route) {
            ReadingsScreenTopLevel(navController, readingsViewModel)
        }

        composable(Screen.AddReading.route) {
            AddReadingScreenTopLevel(navController)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ResitAppTheme(dynamicColor = false) {
        Greeting("Android")
    }
}
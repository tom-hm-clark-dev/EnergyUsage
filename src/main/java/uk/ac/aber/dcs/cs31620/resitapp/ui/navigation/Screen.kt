package uk.ac.aber.dcs.cs31620.resitapp.ui.navigation

sealed class Screen(
    val route: String
) {
    object Home: Screen("home")
    object Readings : Screen("readings")
    object AddReading : Screen("addReading")

}

val screens = listOf(
    Screen.Home,
    Screen.Readings
)

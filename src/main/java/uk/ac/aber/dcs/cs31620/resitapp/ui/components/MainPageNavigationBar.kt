package uk.ac.aber.dcs.cs31620.resitapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.EnergySavingsLeaf
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.EnergySavingsLeaf
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.resitapp.R
import uk.ac.aber.dcs.cs31620.resitapp.ui.home.HomeScreen
import uk.ac.aber.dcs.cs31620.resitapp.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.resitapp.ui.navigation.screens
import uk.ac.aber.dcs.cs31620.resitapp.ui.theme.ResitAppTheme

@Composable
fun MainPageNavigationBar(
    navController: NavController
) {
    val icons = mapOf (
        Screen.Home to IconGroup(
            filledIcon = Icons.Filled.Home,
            outlineIcon = Icons.Outlined.Home,
            label = stringResource(id = R.string.home)
        ),
        Screen.Readings to IconGroup(
            filledIcon = Icons.Filled.EnergySavingsLeaf,
            outlineIcon = Icons.Outlined.EnergySavingsLeaf,
            label = stringResource(id = R.string.readings)
        )
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        screens.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            val labelText = icons[screen]!!.label
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = (if (isSelected)
                            icons[screen]!!.filledIcon
                        else
                            icons[screen]!!.outlineIcon),
                        contentDescription = labelText
                    )
                },
                label = { Text(labelText) },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPageNavigationBarPreview() {
    val navController = rememberNavController()
    ResitAppTheme(dynamicColor = false) {
        MainPageNavigationBar(navController)
    }
}
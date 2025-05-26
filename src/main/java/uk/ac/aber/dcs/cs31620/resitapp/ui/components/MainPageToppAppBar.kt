package uk.ac.aber.dcs.cs31620.resitapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.aber.dcs.cs31620.resitapp.Greeting
import uk.ac.aber.dcs.cs31620.resitapp.R
import uk.ac.aber.dcs.cs31620.resitapp.ui.theme.ResitAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPageTopAppBar(
    onClick: () -> Unit = {}
){
    CenterAlignedTopAppBar(
        title = {
            Text(text = stringResource(R.string.energy_app_title))
        },
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription =
                    stringResource(R.string.app_icon)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MainPageTopAppBarPreview() {
    ResitAppTheme(dynamicColor = false) {
        MainPageTopAppBar()
    }
}
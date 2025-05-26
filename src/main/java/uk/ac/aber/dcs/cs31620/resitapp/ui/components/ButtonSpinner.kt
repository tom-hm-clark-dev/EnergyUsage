package uk.ac.aber.dcs.cs31620.resitapp.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.aber.dcs.cs31620.resitapp.R
import uk.ac.aber.dcs.cs31620.resitapp.ui.theme.ResitAppTheme

@Composable
fun ButtonSpinner(
    items: List<String>,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    itemClick: (String) -> Unit = {}
) {
    var itemText by rememberSaveable { mutableStateOf(if (items.isNotEmpty()) items[0] else "") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    OutlinedButton(
        modifier = modifier,
        onClick = { expanded = !expanded }
    ) {
        Text(
            text = itemText,
            fontSize = fontSize,
            modifier = Modifier.padding(end = 8.dp)
        )

        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = stringResource(R.string.dropdown_icon)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        )
        {
            items.forEach {
                DropdownMenuItem(
                    text = { Text(text = it) },
                    onClick = {
                        //Collapse the dropdown
                        expanded = false

                        // Remember the name of the item selected
                        itemText = it

                        // Hoist the state to the caller
                        itemClick(it)
                    }
                )
            }
        }
    }
}


@Composable
@Preview
private fun SpinnerPreview() {
    ResitAppTheme(dynamicColor = false) {
        val items = listOf("Numbers", "One", "Two")
        ButtonSpinner(items = items)
    }
}

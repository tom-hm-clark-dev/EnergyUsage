package uk.ac.aber.dcs.cs31620.resitapp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.aber.dcs.cs31620.resitapp.model.ReadingSearch

@Composable
fun SearchArea(
    modifier: Modifier = Modifier,
    readingSearch: ReadingSearch,
    nameList: List<String>,
    kwhList: List<String>,
    dateList: List<String>,
    updateSearch: (ReadingSearch) -> Unit = {},
) {

    var dialogIsOpen by rememberSaveable { mutableStateOf(false) }

    Card(
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
    ) {
        Row {
            ButtonSpinner(
                items = nameList,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp),
                itemClick = {
                    updateSearch(
                        ReadingSearch(
                            name = it,
                            kwh = readingSearch.kwh,
                            date = readingSearch.date
                        )
                    )
                }
            )

            ButtonSpinner(
                items = kwhList,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp, end = 8.dp),
                itemClick = {
                    updateSearch(
                        ReadingSearch(
                            name = readingSearch.name,
                            kwh = it,
                            date = readingSearch.date,

                        )
                    )
                }
            )
        }

        Row {
            ButtonSpinner(
                items = dateList,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                itemClick = {
                    updateSearch(
                        ReadingSearch(
                            name = readingSearch.name,
                            kwh = readingSearch.kwh,
                            date = it,
                        )
                    )
                }
            )

          /*  OutlinedButton(
                onClick = {
                    // Changing the state will cause a recomposition of DistanceDialog
                    dialogIsOpen = true
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp, bottom = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.distance, catSearch.distance),
                    fontSize = 16.sp
                )
            }

            DistanceDialog(
                distance = catSearch.distance,
                dialogIsOpen = dialogIsOpen,
                dialogOpen = { isOpen ->
                    dialogIsOpen = isOpen
                },
                changeDistance = {
                    updateSearch(
                        CatSearch(
                            breed = catSearch.breed,
                            gender = catSearch.gender,
                            ageRange = catSearch.ageRange,
                            distance = it
                        )
                    )
                }
            ) */
        }
    }
}

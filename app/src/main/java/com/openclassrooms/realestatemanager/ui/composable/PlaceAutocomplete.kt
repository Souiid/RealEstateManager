package com.openclassrooms.realestatemanager.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.PlacesClient
import com.idrisssouissi.smartbait.presentation.components.ThemeText
import com.idrisssouissi.smartbait.presentation.components.ThemeTextStyle
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.RealtyPlace
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceAutocomplete(
    onSearchPlaces: (PlacesClient, String, (List<AutocompletePrediction>) -> Unit) -> Unit,
    onFetchLatLng: suspend (PlacesClient, String) -> LatLng?,
    callback: (RealtyPlace) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val apiKey = context.getString(R.string.google_maps_key)
    if (!Places.isInitialized()) {
        Places.initialize(context.applicationContext, apiKey)
    }

    val placesClient = remember { Places.createClient(context) }

    Column(
        Modifier
            .padding(16.dp)
            .clickable {
                predictions = emptyList()
            }
    ) {
        TextField(
            value = query,
            onValueChange = { newQuery ->
                query = newQuery
                if (newQuery.isNotEmpty()) {
                    onSearchPlaces(placesClient, newQuery) { results ->
                        predictions = results
                    }
                } else {
                    predictions = emptyList()
                }
            },
            label = {
                ThemeText(
                    text = stringResource(R.string.search_place),
                    style = ThemeTextStyle.NORMAL
                )
            },
            modifier = Modifier
                .fillMaxWidth()
        )

        LazyColumn(
            modifier = Modifier
                .heightIn(max = 200.dp)
        ) {
            items(predictions) { prediction ->
                Text(
                    text = prediction.getPrimaryText(null).toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope.launch {
                                query = prediction.getFullText(null).toString()
                                val position =
                                    onFetchLatLng(placesClient, prediction.placeId) ?: LatLng(
                                        0.0,
                                        0.0
                                    )
                                callback(RealtyPlace(prediction.placeId, query, position))
                                predictions = emptyList()
                            }
                        }
                        .padding(8.dp)
                )
            }
        }
    }
}
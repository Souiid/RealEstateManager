package com.openclassrooms.realestatemanager.ui.screens.form.realtyform

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.RealtyPlace
import com.openclassrooms.realestatemanager.data.RealtyPrimaryInfo
import com.openclassrooms.realestatemanager.data.RealtyType
import com.openclassrooms.realestatemanager.ui.composable.ThemeButton
import com.openclassrooms.realestatemanager.ui.composable.ThemeDialog
import com.openclassrooms.realestatemanager.ui.composable.ThemeOutlinedTextField
import com.openclassrooms.realestatemanager.ui.composable.ThemeTopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealtyFormScreen(viewModel: RealtyFormViewModel, onNext: () -> Unit, onBack: ()-> Unit) {

    var surfaceValue by remember { mutableStateOf("") }
    var priceValue by remember { mutableStateOf("") }
    var roomsNbrValue by remember { mutableStateOf("") }
    var bedRoomNbrValue by remember { mutableStateOf("") }
    var bathRoomNbrValue by remember { mutableStateOf("") }
    var descriptionValue by remember { mutableStateOf("") }
    var realtyPlaceValue by remember { mutableStateOf<RealtyPlace?>(null) }
    var displayDialog by remember { mutableStateOf(false) }

    val options =
        RealtyType.entries.map { it -> it.name.lowercase().replaceFirstChar { it.uppercase() } }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options[0]) }

    val realtyPrimaryInfo = viewModel.getPrimaryInfo()

    if (realtyPrimaryInfo != null) {
        surfaceValue = realtyPrimaryInfo.surface.toString()
        priceValue = realtyPrimaryInfo.price.toString()
        roomsNbrValue = realtyPrimaryInfo.roomsNbr.toString()
        descriptionValue = realtyPrimaryInfo.description
        realtyPlaceValue = realtyPrimaryInfo.realtyPlace
        bedRoomNbrValue = realtyPrimaryInfo.bedroomsNbr.toString()
        bathRoomNbrValue = realtyPrimaryInfo.bathroomsNbr.toString()
        realtyPlaceValue = realtyPrimaryInfo.realtyPlace
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = { ThemeTopBar(title = "Realty form", onBackClick = { onBack() }) },
        bottomBar = {
            ThemeButton(
                onClick = {
                    if (viewModel.isFormValid(
                            surface = surfaceValue,
                            price = priceValue,
                            rooms = roomsNbrValue,
                            description = descriptionValue,
                            realtyPlace = realtyPlaceValue
                        )
                    ) {
                        viewModel.setPrimaryInfo(
                            RealtyPrimaryInfo(
                                realtyType = RealtyType.valueOf(selectedOption.uppercase()),
                                surface = surfaceValue.toDouble(),
                                price = priceValue.toDouble(),
                                roomsNbr = roomsNbrValue.toInt(),
                                bathroomsNbr = bathRoomNbrValue.toInt(),
                                bedroomsNbr = bedRoomNbrValue.toInt(),
                                description = descriptionValue,
                                realtyPlace = realtyPlaceValue ?: return@ThemeButton
                            )
                        )
                        onNext()
                    } else {
                        displayDialog = true
                    }
                },
                text = "Next step",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedOption,
                    onValueChange = {},
                    label = {
                        Text(text = "Type")
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = DarkGray,
                        fontSize = 16.sp
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Green,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedOption = option
                                expanded = false
                            }
                        )
                    }
                }
            }

            ThemeOutlinedTextField(
                value = surfaceValue,
                onValueChanged = { surfaceValue = it },
                labelID = R.string.surface,
                imeAction = ImeAction.Next,
                iconText = "m²",
                keyboardType = KeyboardType.Number
            )
            ThemeOutlinedTextField(
                value = priceValue,
                onValueChanged = { priceValue = it },
                labelID = R.string.price,
                imeAction = ImeAction.Next,
                iconText = "€",
                keyboardType = KeyboardType.Number
            )
            ThemeOutlinedTextField(
                value = roomsNbrValue,
                onValueChanged = { roomsNbrValue = it },
                labelID = R.string.number_of_rooms,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            )

            ThemeOutlinedTextField(
                value = bedRoomNbrValue,
                onValueChanged = { bedRoomNbrValue = it },
                labelID = R.string.number_of_bedrooms,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            )

            ThemeOutlinedTextField(
                value = bathRoomNbrValue,
                onValueChanged = { bathRoomNbrValue = it },
                labelID = R.string.number_of_bathrooms,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            )
            ThemeOutlinedTextField(
                value = descriptionValue,
                onValueChanged = { descriptionValue = it },
                labelID = R.string.description_of_realty,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text,
                singleLine = false,
                height = 150.dp
            )

            PlaceAutocompleteTest(viewModel, callback = { place ->
                realtyPlaceValue = place
            })

            if (displayDialog) {
                ThemeDialog(
                    title = "Dialog",
                    description = "Please fill all fields",
                    primaryButtonTitle = "Ok",
                    onPrimaryButtonClick = { displayDialog = false },
                    onDismissRequest = {})
            }
        }
    }
}

@Composable
fun PlaceAutocompleteTest(viewModel: RealtyFormViewModel, callback: (RealtyPlace) -> Unit) {
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
                Log.d("aaa", "Query : $query")
                if (newQuery.isNotEmpty()) {
                    viewModel.searchPlaces(placesClient, newQuery) { results ->
                        predictions = results

                    }
                } else {
                    predictions = emptyList()
                }
            },
            label = { Text("Search Places") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    predictions = emptyList()
                }
        )

        LazyColumn {
            items(predictions) { prediction ->
                Text(
                    text = prediction.getPrimaryText(null).toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope.launch {
                                query = prediction
                                    .getFullText(null)
                                    .toString()
                                val position = viewModel.fetchPlaceLatLng(
                                    placesClient,
                                    prediction.placeId
                                ) ?: LatLng(0.0, 0.0)
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


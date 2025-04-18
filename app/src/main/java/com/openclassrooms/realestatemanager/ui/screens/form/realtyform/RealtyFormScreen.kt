package com.openclassrooms.realestatemanager.ui.screens.form.realtyform

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.RealtyPlace
import com.openclassrooms.realestatemanager.data.RealtyPrimaryInfo
import com.openclassrooms.realestatemanager.data.RealtyType
import com.openclassrooms.realestatemanager.data.room.Amenity
import com.openclassrooms.realestatemanager.ui.composable.ThemeButton
import com.openclassrooms.realestatemanager.ui.composable.ThemeDialog
import com.openclassrooms.realestatemanager.ui.composable.ThemeOutlinedTextField
import com.openclassrooms.realestatemanager.ui.composable.ThemeTopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealtyFormScreen(viewModel: RealtyFormViewModel, onNext: () -> Unit, onBack: () -> Unit) {

    var surfaceValue by remember { mutableStateOf("") }
    var priceValue by remember { mutableStateOf("") }
    var roomsNbrValue by remember { mutableStateOf("") }
    var bedRoomNbrValue by remember { mutableStateOf("") }
    var bathRoomNbrValue by remember { mutableStateOf("") }
    var descriptionValue by remember { mutableStateOf("") }
    var realtyPlaceValue by remember { mutableStateOf<RealtyPlace?>(null) }
    var displayDialog by remember { mutableStateOf(false) }

    val options = RealtyType.entries
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options[0]) }
    val amenityList = Amenity.entries
    var selectedAmenities by remember { mutableStateOf(emptyList<Amenity>()) }

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
        topBar = { ThemeTopBar(title = stringResource(R.string.realty_form), onBackClick = { onBack() }) },
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
                                realtyType = selectedOption,
                                surface = surfaceValue.toDouble(),
                                price = priceValue.toDouble(),
                                roomsNbr = roomsNbrValue.toInt(),
                                bathroomsNbr = bathRoomNbrValue.toInt(),
                                bedroomsNbr = bedRoomNbrValue.toInt(),
                                description = descriptionValue,
                                realtyPlace = realtyPlaceValue ?: return@ThemeButton,
                                amenities = selectedAmenities
                            )
                        )
                        onNext()
                    } else {
                        displayDialog = true
                    }
                },
                text = stringResource(R.string.next_step),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }) { paddingValues ->

        if (displayDialog) {
            ThemeDialog(
                title = stringResource(R.string.error),
                description = stringResource(R.string.fill_all_fields),
                primaryButtonTitle = stringResource(R.string.ok),
                onPrimaryButtonClick = { displayDialog = false },
                onDismissRequest = {})
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            item {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = stringResource(selectedOption.labelResId),
                        onValueChange = {},
                        label = {
                            Text(text = stringResource(R.string.type))
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
                                text = { Text(stringResource(option.labelResId)) },
                                onClick = {
                                    selectedOption = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            item {
                ThemeOutlinedTextField(
                    value = surfaceValue,
                    onValueChanged = { surfaceValue = it },
                    labelID = R.string.surface,
                    imeAction = ImeAction.Next,
                    iconText = "m²",
                    keyboardType = KeyboardType.Number
                )
            }

            item {
                ThemeOutlinedTextField(
                    value = priceValue,
                    onValueChanged = { priceValue = it },
                    labelID = R.string.price,
                    imeAction = ImeAction.Next,
                    iconText = "€",
                    keyboardType = KeyboardType.Number
                )
            }

            item {
                ThemeOutlinedTextField(
                    value = roomsNbrValue,
                    onValueChanged = { roomsNbrValue = it },
                    labelID = R.string.number_of_rooms,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                )
            }

            item {
                ThemeOutlinedTextField(
                    value = bedRoomNbrValue,
                    onValueChanged = { bedRoomNbrValue = it },
                    labelID = R.string.number_of_bedrooms,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                )
            }

            item {
                ThemeOutlinedTextField(
                    value = bathRoomNbrValue,
                    onValueChanged = { bathRoomNbrValue = it },
                    labelID = R.string.number_of_bathrooms,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                )
            }

            item {
                ThemeOutlinedTextField(
                    value = descriptionValue,
                    onValueChanged = { descriptionValue = it },
                    labelID = R.string.description_of_realty,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text,
                    singleLine = false,
                    height = 150.dp
                )
            }

            item {
                PlaceAutocompleteTest(viewModel, callback = { place ->
                    realtyPlaceValue = place
                })
            }

            item {
                SelectableChipsGroup(
                    options = amenityList.map { it to stringResource(it.labelResId) },
                    selectedOptions = selectedAmenities,
                    onSelectionChanged = { selectedAmenities = it }
                )
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
            label = { Text(stringResource(R.string.search_place)) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    predictions = emptyList()
                }
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

@Composable
fun SelectableChipsGroup(
    options: List<Pair<Amenity, String>>,
    selectedOptions: List<Amenity>,
    onSelectionChanged: (List<Amenity>) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp
    ) {
        options.forEach { (amenity, label) ->
            val isSelected = amenity in selectedOptions
            Chip(
                label = label,
                selected = isSelected,
                onClick = {
                    val updated = if (isSelected) {
                        selectedOptions - amenity
                    } else {
                        selectedOptions + amenity
                    }
                    onSelectionChanged(updated)
                }
            )
        }
    }
}

@Composable
fun Chip(label: String, selected: Boolean, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = true,
                onClick = { onClick() }
            ),
        shape = RoundedCornerShape(50),
        color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent,
        border = BorderStroke(
            1.dp,
            if (selected) MaterialTheme.colorScheme.primary else Color.Gray
        )) {
        Text(
            text = label,
            color = if (selected) MaterialTheme.colorScheme.primary else Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(6.dp)
        )
    }
}


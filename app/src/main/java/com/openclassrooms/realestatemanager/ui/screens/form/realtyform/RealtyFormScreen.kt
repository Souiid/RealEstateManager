package com.openclassrooms.realestatemanager.ui.screens.form.realtyform

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.RealtyPlace
import com.openclassrooms.realestatemanager.data.RealtyPrimaryInfo
import com.openclassrooms.realestatemanager.data.RealtyType
import com.openclassrooms.realestatemanager.data.room.Amenity
import com.openclassrooms.realestatemanager.ui.composable.PlaceAutocomplete
import com.openclassrooms.realestatemanager.ui.composable.SelectableChipsGroup
import com.openclassrooms.realestatemanager.ui.composable.ThemeButton
import com.openclassrooms.realestatemanager.ui.composable.ThemeDialog
import com.openclassrooms.realestatemanager.ui.composable.ThemeOutlinedTextField
import com.openclassrooms.realestatemanager.ui.composable.ThemeTopBar

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
    var selectedAmenities by remember { mutableStateOf(emptyList<Amenity>()) }

    val realtyPrimaryInfo = viewModel.getPrimaryInfo()

    if (realtyPrimaryInfo != null) {
        surfaceValue = "${realtyPrimaryInfo.surface}"
        priceValue = "${realtyPrimaryInfo.price}"
        roomsNbrValue = realtyPrimaryInfo.roomsNbr.toString()
        descriptionValue = realtyPrimaryInfo.description
        realtyPlaceValue = realtyPrimaryInfo.realtyPlace
        bedRoomNbrValue = realtyPrimaryInfo.bedroomsNbr.toString()
        bathRoomNbrValue = realtyPrimaryInfo.bathroomsNbr.toString()
        realtyPlaceValue = realtyPrimaryInfo.realtyPlace
    }

    val updatedRealty = viewModel.getRealtyFromRealtyRepository()
    Log.d("aaa", "Updated Realty : $updatedRealty")
    LaunchedEffect(updatedRealty) {
        updatedRealty?.let {
            val surface = it.primaryInfo.surface
            val price = it.primaryInfo.price
            val roomsNbr = it.primaryInfo.roomsNbr
            val description = it.primaryInfo.description
            val realtyPlace = it.primaryInfo.realtyPlace
            val bedRoomNbr = it.primaryInfo.bedroomsNbr
            val bathRoomNbr = it.primaryInfo.bathroomsNbr
            val type = it.primaryInfo.realtyType
            val amenities = it.primaryInfo.amenities

            surfaceValue = surface.toString()
            priceValue = price.toString()
            roomsNbrValue = roomsNbr.toString()
            descriptionValue = description
            realtyPlaceValue = realtyPlace
            bedRoomNbrValue = bedRoomNbr.toString()
            bathRoomNbrValue = bathRoomNbr.toString()
            selectedOption = type
            selectedAmenities = amenities
        }
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
                            updatedRealty = updatedRealty,
                            realtyPrimaryInfo = RealtyPrimaryInfo(
                                realtyType = selectedOption,
                                surface = surfaceValue.toInt(),
                                price = priceValue.toInt(),
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

            if (updatedRealty == null) {
                item {
                   PlaceAutocomplete(
                        onSearchPlaces = { placesClient, query, callback ->
                            viewModel.searchPlaces(placesClient, query, callback)
                        },
                        onFetchLatLng = { placesClient, placeId ->
                            viewModel.fetchPlaceLatLng(placesClient, placeId)
                        },
                        callback = { selectedPlace ->
                            realtyPlaceValue = selectedPlace
                        }
                    )
                }
            }

            item {
                SelectableChipsGroup(
                    selectedOptions = selectedAmenities,
                    onSelectionChanged = { selectedAmenities = it }
                )
            }
        }
    }
}

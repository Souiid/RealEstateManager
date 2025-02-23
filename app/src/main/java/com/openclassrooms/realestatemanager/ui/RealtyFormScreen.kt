package com.openclassrooms.realestatemanager.ui

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.RealtyPlace
import com.openclassrooms.realestatemanager.data.RealtyPrimaryInfo
import com.openclassrooms.realestatemanager.data.RealtyType
import com.openclassrooms.realestatemanager.ui.composable.BaseScreen
import com.openclassrooms.realestatemanager.ui.composable.ThemeButton
import com.openclassrooms.realestatemanager.ui.composable.ThemeDialog
import com.openclassrooms.realestatemanager.ui.composable.ThemeOutlinedTextField
import com.openclassrooms.realestatemanager.ui.composable.ThemeTopBar
import org.koin.androidx.compose.koinViewModel


class RealtyFormScreen() : BaseScreen<RealtyFormScreen.Params>() {

    var onNext: () -> Unit = {}
    lateinit var viewModel: RealtyFormViewModel

    data class Params(val onNext: () -> Unit)

    override fun setParameters(params: Params) {
        params.let {
            onNext = it.onNext
        }
    }

    @Composable
    override fun InitViewModel() {
        viewModel = koinViewModel()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun ScreenInternal() {

        val surfaceValue = remember { mutableStateOf("") }
        val priceValue = remember { mutableStateOf("") }
        val numberOfRoomsValue = remember { mutableStateOf("") }
        val descriptionValue = remember { mutableStateOf("") }
        var realtyPlaceValue = remember { mutableStateOf<RealtyPlace?>(null) }
        var displayDialog by remember { mutableStateOf(false) }

        val options =
            RealtyType.entries.map { it -> it.name.lowercase().replaceFirstChar { it.uppercase() } }
        var expanded by remember { mutableStateOf(false) }
        var selectedOption by remember { mutableStateOf(options[0]) }

        val realtyPrimaryInfo = viewModel.getPrimaryInfo()
        if (realtyPrimaryInfo != null) {
            surfaceValue.value = realtyPrimaryInfo.surface.toString()
            priceValue.value = realtyPrimaryInfo.price.toString()
            numberOfRoomsValue.value = realtyPrimaryInfo.rooms.toString()
            descriptionValue.value = realtyPrimaryInfo.description
            realtyPlaceValue.value = realtyPrimaryInfo.realtyPlace
        }


        Scaffold(modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
            topBar = { ThemeTopBar(title = "Realty form", onBackClick = { }) },
            bottomBar = {
                ThemeButton(
                    onClick = {
                        if (viewModel.isFormValid(
                                surface = surfaceValue.value,
                                price = priceValue.value,
                                rooms = numberOfRoomsValue.value,
                                description = descriptionValue.value,
                                realtyPlace = realtyPlaceValue.value
                            )
                        ) {
                            viewModel.setPrimaryInfo(
                                RealtyPrimaryInfo(
                                    realtyType = RealtyType.valueOf(selectedOption.uppercase()),
                                    surface = surfaceValue.value.toDouble(),
                                    price = priceValue.value.toDouble(),
                                    rooms = numberOfRoomsValue.value.toInt(),
                                    description = descriptionValue.value,
                                    realtyPlace = realtyPlaceValue.value!!)
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
                    textFieldValue = surfaceValue,
                    labelID = R.string.surface,
                    imeAction = ImeAction.Next,
                    iconText = "m²",
                    keyboardType = KeyboardType.Number
                )
                ThemeOutlinedTextField(
                    textFieldValue = priceValue,
                    labelID = R.string.price,
                    imeAction = ImeAction.Next,
                    iconText = "€",
                    keyboardType = KeyboardType.Number
                )
                ThemeOutlinedTextField(
                    textFieldValue = numberOfRoomsValue,
                    labelID = R.string.number_of_rooms,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                )
                ThemeOutlinedTextField(
                    textFieldValue = descriptionValue,
                    labelID = R.string.description_of_realty,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text,
                    singleLine = false,
                    height = 150.dp
                )

                PlaceAutocompleteTest(callback = { place ->
                    realtyPlaceValue.value = place
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
    fun PlaceAutocompleteTest(callback: (RealtyPlace) -> Unit) {
        var query by remember { mutableStateOf("") }
        var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
        val context = LocalContext.current
        val apiKey = context.getString(R.string.google_maps_key)
        if (!Places.isInitialized()) {
            Places.initialize(context.applicationContext, apiKey)
        }

        val placesClient = remember { Places.createClient(context) }

        Column(
            Modifier
                .padding(16.dp)
                .clickable {
                    // Dismiss predictions when clicking outside
                    predictions = emptyList()
                }
        ) {
            TextField(
                value = query,
                onValueChange = { newQuery ->
                    query = newQuery
                    Log.d("aaa", "Query : $query")
                    if (newQuery.isNotEmpty()) {
                        searchPlaces(placesClient, newQuery) { results ->
                            predictions = results

                        }
                    } else {
                        predictions = emptyList() // Hide suggestions when query is empty
                    }
                },
                label = { Text("Search Places") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // Dismiss predictions when clicking outside
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
                                query = prediction
                                    .getFullText(null)
                                    .toString()
                                callback(RealtyPlace(prediction.placeId, query))
                                predictions = emptyList() // Hide the list after selection
                            }
                            .padding(8.dp)
                    )
                }
            }
        }
    }

    private fun searchPlaces(
        placesClient: PlacesClient,
        query: String,
        onResults: (List<AutocompletePrediction>) -> Unit
    ) {
        if (query.isEmpty()) {
            onResults(emptyList())
            return
        }

        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                val predictions = response.autocompletePredictions
                onResults(predictions)
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                onResults(emptyList())
            }
    }

    override fun screenLifeCycle(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event) {
        //
    }

}
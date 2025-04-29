package com.openclassrooms.realestatemanager.ui.screens.search

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.RealtyType
import com.openclassrooms.realestatemanager.data.SearchCriteria
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import com.openclassrooms.realestatemanager.ui.composable.AgentDropdown
import com.openclassrooms.realestatemanager.ui.composable.PriceTextField
import com.openclassrooms.realestatemanager.ui.composable.SelectableChipsGroup
import com.openclassrooms.realestatemanager.ui.composable.ThemeButton
import com.openclassrooms.realestatemanager.ui.composable.ThemeOutlinedTFForDPD
import com.openclassrooms.realestatemanager.ui.composable.ThemeOutlinedTextField
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.ZoneId
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel = koinViewModel()) {
    val criteria = viewModel.getCriteria()

    var selectedRealtyTypes by remember { mutableStateOf(criteria?.realtyTypes ?: emptyList()) }
    var selectedStatus by remember { mutableStateOf<Boolean?>(criteria?.isAvailable) }
    var minPriceValue by remember { mutableStateOf<Int?>(criteria?.minPrice) }
    var maxPriceValue by remember { mutableStateOf<Int?>(criteria?.maxPrice) }
    val currency = "$"

    var minSurfaceValue by remember { mutableStateOf<Int?>(criteria?.minSurface) }
    var maxSurfaceValue by remember { mutableStateOf<Int?>(criteria?.maxSurface) }

    val datePickerState = rememberDatePickerState()
    var minEntryDateValue by remember { mutableStateOf<Date?>(criteria?.minEntryDate) }
    var maxEntryDateValue by remember { mutableStateOf<Date?>(criteria?.maxEntryDate) }
    var minSoldDateValue by remember { mutableStateOf<Date?>(criteria?.minSoldDate) }
    var maxSoldDateValue by remember { mutableStateOf<Date?>(criteria?.maxSoldDate) }
    var selectedAmenities by remember { mutableStateOf(criteria?.amenities ?: emptyList()) }

    val agents by viewModel.agentsFlow.collectAsState(initial = emptyList())
    var selectedAgent by remember { mutableStateOf<RealtyAgent?>(criteria?.selectedAgent) }

    var minNumberOfRooms by remember { mutableStateOf<Int?>(criteria?.minRooms) }
    var maxNumberOfRooms by remember { mutableStateOf<Int?>(criteria?.minRooms) }

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        bottomBar = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ThemeButton(
                    onClick = {
                        selectedRealtyTypes = emptyList()
                        selectedStatus = null
                        minPriceValue = null
                        maxPriceValue = null
                        minSurfaceValue = null
                        maxSurfaceValue = null
                        minEntryDateValue = null
                        maxEntryDateValue = null
                        minSoldDateValue = null
                        maxSoldDateValue = null
                        selectedAmenities = emptyList()
                        selectedAgent = null
                        minNumberOfRooms = 0
                        maxNumberOfRooms = 0
                        viewModel.setCriteria(
                            SearchCriteria(
                                realtyTypes = emptyList(),
                                isAvailable = null,
                                minPrice = null,
                                maxPrice = null,
                                minSurface = null,
                                maxSurface = null,
                                minEntryDate = null,
                                maxEntryDate = null,
                                minSoldDate = null,
                                maxSoldDate = null,
                                amenities = emptyList(),
                                selectedAgent = null,
                                minRooms = 0,
                                maxRooms = 0
                            )
                        )
                        viewModel.performSearch(isReset = true)
                    },
                    text = stringResource(R.string.reset),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(5.dp))

                ThemeButton(
                    onClick = {

                        viewModel.setCriteria(
                            SearchCriteria(
                                realtyTypes = selectedRealtyTypes,
                                isAvailable = selectedStatus,
                                minPrice = minPriceValue,
                                maxPrice = maxPriceValue,
                                minSurface = minSurfaceValue,
                                maxSurface = maxSurfaceValue,
                                minEntryDate = minEntryDateValue,
                                maxEntryDate = maxEntryDateValue,
                                minSoldDate = minSoldDateValue,
                                maxSoldDate = maxSoldDateValue,
                                amenities = selectedAmenities,
                                selectedAgent = selectedAgent,
                                minRooms = minNumberOfRooms,
                                maxRooms = maxNumberOfRooms
                            )
                        )

                        viewModel.performSearch(
                            isAvailable = selectedStatus,
                            minPrice = minPriceValue?.toDouble(),
                            maxPrice = maxPriceValue?.toDouble(),
                            minSurface = minSurfaceValue?.toDouble(),
                            maxSurface = maxSurfaceValue?.toDouble(),
                            minRooms = minNumberOfRooms,
                            maxRooms = maxNumberOfRooms,
                            minEntryDate = minEntryDateValue,
                            maxEntryDate = maxEntryDateValue,
                            minSoldDate = minSoldDateValue,
                            maxSoldDate = maxSoldDateValue,
                            realtyTypes = selectedRealtyTypes.takeIf { it.isNotEmpty() == true }?.map { it.name },
                            amenity = selectedAmenities.firstOrNull()?.name
                        )
                    },
                    text = stringResource(R.string.apply),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                StatusSegmentedButton(
                    selectedStatus = selectedStatus,
                    onStatusSelected = { selectedStatus = it }
                )
                HorizontalDivider()
            }

            item {
                RealtyTypeSelection(
                    selectedTypes = selectedRealtyTypes,
                    onSelectionChanged = { selectedRealtyTypes = it }
                )
                HorizontalDivider()
            }

            item {
                SetPriceFilterTextFields(
                    minPriceValue = minPriceValue,
                    maxPriceValue = maxPriceValue,
                    onMinPriceChange = { minPriceValue = it },
                    onMaxPriceChange = { maxPriceValue = it },
                    currencyP = currency
                )
                HorizontalDivider()
            }

            item {
                SetSurfaceFilterTextFields(
                    minSurfaceValue = minSurfaceValue,
                    maxSurfaceValue = maxSurfaceValue,
                    onMinSurfaceChange = { minSurfaceValue = it },
                    onMaxSurfaceChange = { maxSurfaceValue = it }
                )
                HorizontalDivider()
            }


            item {
                DatePickerDialog(
                    labelID = R.string.min_entry_date,
                    datePickerState = datePickerState,
                    onDateSelected = {
                        minEntryDateValue = it
                    }
                )
                Spacer(modifier = Modifier.height(5.dp))

                DatePickerDialog(
                    labelID = R.string.max_entry_date,
                    datePickerState = datePickerState,
                    onDateSelected = {
                        maxEntryDateValue = it
                    }
                )
            }

            item {
                DatePickerDialog(
                    labelID = R.string.min_sold_date,
                    datePickerState = datePickerState,
                    onDateSelected = {
                        minSoldDateValue = it
                    }
                )

                DatePickerDialog(
                    labelID = R.string.max_sold_date,
                    datePickerState = datePickerState,
                    onDateSelected = {
                        maxSoldDateValue = it
                    }
                )
            }

            item {
                Text(stringResource(R.string.amenities))
                SelectableChipsGroup(
                    selectedOptions = selectedAmenities,
                    onSelectionChanged = { selectedAmenities = it }
                )
            }

            item {
                AgentDropdown(
                    agents = agents,
                    selectedAgent = selectedAgent,
                    onAgentSelected = { selectedAgent = it },
                    isForSearch = true
                )
            }

            item {
                NumberOfRooms(value = minNumberOfRooms.toString()) {
                    minNumberOfRooms = it.toIntOrNull() ?: 0
                }

                Spacer(modifier = Modifier.height(5.dp))

                NumberOfRooms(value = minNumberOfRooms.toString()) {
                    minNumberOfRooms = it.toIntOrNull() ?: 0
                }
            }

        }
    }
}

@Composable
fun NumberOfRooms(value: String, onValueChanged: (String) -> Unit) {

    Text("Number of rooms")
    ThemeOutlinedTextField(
        value = value,
        onValueChanged = { onValueChanged(it) },
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Number,
        labelID = R.string.number_of_rooms,
        modifier = Modifier.fillMaxWidth()
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    labelID: Int,
    datePickerState: DatePickerState,
    onDateSelected: (Date) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedDateText by remember { mutableStateOf("") }
    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val localDate = Instant.ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        selectedDateText =
                            "${localDate.dayOfMonth}/${localDate.monthValue}/${localDate.year}"
                        onDateSelected(Date(it))
                    }
                    showDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Annuler")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    ThemeOutlinedTFForDPD(
        value = selectedDateText,
        labelID = labelID,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { showDialog = true }
    )
}

@Composable
fun SetSurfaceFilterTextFields(
    minSurfaceValue: Int?,
    maxSurfaceValue: Int?,
    onMinSurfaceChange: (Int?) -> Unit,
    onMaxSurfaceChange: (Int?) -> Unit
) {


    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        ThemeOutlinedTextField(
            value = minSurfaceValue?.toString() ?: "",
            onValueChanged = { onMinSurfaceChange(it.toIntOrNull()) },
            labelID = R.string.min_surface,
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number,
            modifier = Modifier.weight(1f),
            iconText = "m²"
        )

        ThemeOutlinedTextField(
            value = maxSurfaceValue?.toString() ?: "",
            onValueChanged = { onMaxSurfaceChange(it.toIntOrNull()) },
            labelID = R.string.max_surface,
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number,
            modifier = Modifier.weight(1f),
            iconText = "m²"
        )

    }
}

@Composable
fun SetPriceFilterTextFields(
    minPriceValue: Int?,
    maxPriceValue: Int?,
    onMinPriceChange: (Int?) -> Unit,
    onMaxPriceChange: (Int?) -> Unit,
    currencyP: String
) {

    var currency by remember { mutableStateOf(currencyP) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        PriceTextField(
            value = minPriceValue.toString(),
            onValueChange = { onMinPriceChange(it.toIntOrNull()) },
            currency = currency,
            labelID = R.string.min_price,
            onCurrencyChange = {
                currency = it
            },
            modifier = Modifier.weight(1f)
        )

        PriceTextField(
            value = maxPriceValue.toString(),
            onValueChange = { onMaxPriceChange(it.toIntOrNull()) },
            currency = currency,
            labelID = R.string.max_price,
            onCurrencyChange = {
                currency = it
            },
            modifier = Modifier.weight(1f)
        )

    }
}

@Composable
fun StatusSegmentedButton(
    selectedStatus: Boolean?,
    onStatusSelected: (Boolean?) -> Unit
) {
    val statusList = listOf(
        stringResource(R.string.all) to null,
        stringResource(R.string.available) to true,
        stringResource(R.string.for_sale) to false
    )

    val selectedIndex = statusList.indexOfFirst { it.second == selectedStatus }

    Text(stringResource(R.string.realty_status))

    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        statusList.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = statusList.size
                ),
                onClick = { onStatusSelected(label.second) },
                selected = index == selectedIndex,
            ) {
                Text(label.first)
            }
        }
    }
}

@Composable
fun RealtyTypeSelection(
    selectedTypes: List<RealtyType>,
    onSelectionChanged: (List<RealtyType>) -> Unit
) {
    val checkList = remember(selectedTypes) {
        mutableStateListOf(
            *RealtyType.entries.map { it in selectedTypes }.toTypedArray()
        )
    }

    Column(
        modifier = Modifier
            .heightIn(max = 200.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(stringResource(R.string.realty_type))
        RealtyType.entries.forEachIndexed { index, realtyType ->
            RealtyTypeItem(
                realtyTypeName = realtyType.name,
                isChecked = checkList[index],
                onCheckedChange = {
                    checkList[index] = it
                    val selectedList = RealtyType.entries.filterIndexed { i, _ -> checkList[i] }
                    onSelectionChanged(selectedList)
                }
            )
        }
    }
}

@Composable
fun RealtyTypeItem(realtyTypeName: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = realtyTypeName)
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}


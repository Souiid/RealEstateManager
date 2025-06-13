package com.openclassrooms.realestatemanager.ui.screens.search

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.idrisssouissi.smartbait.presentation.components.ThemeText
import com.idrisssouissi.smartbait.presentation.components.ThemeTextStyle
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.RealtyType
import com.openclassrooms.realestatemanager.data.SearchCriteria
import com.openclassrooms.realestatemanager.data.Utils
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import com.openclassrooms.realestatemanager.ui.composable.AgentDropdown
import com.openclassrooms.realestatemanager.ui.composable.ExpandableSection
import com.openclassrooms.realestatemanager.ui.composable.SelectableChipsGroup
import com.openclassrooms.realestatemanager.ui.composable.ThemeButton
import com.openclassrooms.realestatemanager.ui.composable.ThemeOutlinedTFForDPD
import com.openclassrooms.realestatemanager.ui.composable.ThemeOutlinedTextField
import com.openclassrooms.realestatemanager.ui.screens.CurrencyViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.ZoneId
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = koinViewModel(),
    currencyViewModel: CurrencyViewModel = koinViewModel(),
    onNewSearchCriteria: (SearchCriteria) -> Unit
) {
    val criteria by searchViewModel.criteriaFlow.collectAsState()

    var selectedRealtyTypes by remember(criteria) {
        mutableStateOf(
            criteria?.realtyTypes ?: emptyList()
        )
    }
    var selectedStatus by remember(criteria) { mutableStateOf(criteria?.isAvailable) }
    var minPriceValue by remember(criteria) { mutableStateOf(criteria?.minPrice) }
    var maxPriceValue by remember(criteria) { mutableStateOf(criteria?.maxPrice) }

    var minSurfaceValue by remember(criteria) { mutableStateOf(criteria?.minSurface) }
    var maxSurfaceValue by remember(criteria) { mutableStateOf(criteria?.maxSurface) }

    val datePickerState = rememberDatePickerState()
    var minEntryDateValue by remember(criteria) { mutableStateOf(criteria?.minEntryDate) }
    var maxEntryDateValue by remember(criteria) { mutableStateOf(criteria?.maxEntryDate) }
    var minSoldDateValue by remember(criteria) { mutableStateOf(criteria?.minSoldDate) }
    var maxSoldDateValue by remember(criteria) { mutableStateOf(criteria?.maxSoldDate) }
    var selectedAmenities by remember(criteria) {
        mutableStateOf(
            criteria?.amenities ?: emptyList()
        )
    }

    val agents by searchViewModel.agentsFlow.collectAsState(initial = emptyList())
    var selectedAgent by remember(criteria) { mutableStateOf(criteria?.selectedAgent) }

    var minNumberOfRooms by remember(criteria) { mutableStateOf(criteria?.minRooms) }
    var maxNumberOfRooms by remember(criteria) { mutableStateOf(criteria?.maxRooms) }

    val isEuro by currencyViewModel.isEuroFlow.collectAsState()

    val context = LocalContext.current

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

                        val resetCriteria = SearchCriteria(
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
                            minRooms = null,
                            maxRooms = null,
                        )

                        searchViewModel.setCriteria(resetCriteria)
                        onNewSearchCriteria(resetCriteria)
                    },
                    text = stringResource(R.string.reset),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(5.dp))

                ThemeButton(
                    onClick = {
                        val errorMessage = searchViewModel.validateCriteria(
                            context,
                            minPriceValue, maxPriceValue,
                            minSurfaceValue, maxSurfaceValue,
                            minNumberOfRooms, maxNumberOfRooms,
                            minEntryDateValue, maxEntryDateValue,
                            minSoldDateValue, maxSoldDateValue
                        )

                        if (errorMessage != null) {
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                            return@ThemeButton
                        }


                        var minPrice: Int? = null
                        minPriceValue?.let {

                            minPrice = Utils().getCorrectPriceComponent(it).price
                        }

                        var maxPrice: Int? = null
                        maxPriceValue?.let {
                            maxPrice = Utils().getCorrectPriceComponent(it).price
                        }

                        val newCriteria = SearchCriteria(
                            realtyTypes = selectedRealtyTypes,
                            isAvailable = selectedStatus,
                            minPrice = minPrice,
                            maxPrice = maxPrice,
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

                        searchViewModel.setCriteria(newCriteria)
                        onNewSearchCriteria(newCriteria)
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
                ExpandableSection(
                    title = stringResource(R.string.realty_status),
                    expandedP = true
                ) {
                    StatusSegmentedButton(
                        selectedStatus = selectedStatus,
                        onStatusSelected = { selectedStatus = it }
                    )
                }
            }

            item {
                ExpandableSection(
                    stringResource(R.string.realty_type),
                    expandedP = selectedRealtyTypes.isNotEmpty()
                ) {
                    RealtyTypeSelection(
                        selectedTypes = selectedRealtyTypes,
                        onSelectionChanged = { selectedRealtyTypes = it }
                    )
                }
            }

            item {
                ExpandableSection(stringResource(R.string.price),
                    expandedP = minPriceValue != null || maxPriceValue != null
                ) {
                    SetPriceFilterTextFields(
                        minPriceValue = minPriceValue,
                        maxPriceValue = maxPriceValue,
                        onMinPriceChange = { minPriceValue = it },
                        onMaxPriceChange = { maxPriceValue = it },
                        isEuro = isEuro
                    )
                }
            }

            item {
                ExpandableSection(stringResource(R.string.surface),
                    expandedP = minSurfaceValue != null || maxSurfaceValue != null) {
                    SetSurfaceFilterTextFields(
                        minSurfaceValue = minSurfaceValue,
                        maxSurfaceValue = maxSurfaceValue,
                        onMinSurfaceChange = { minSurfaceValue = it },
                        onMaxSurfaceChange = { maxSurfaceValue = it }
                    )
                }
            }


            item {
                ExpandableSection(stringResource(R.string.entry_date),
                    expandedP = minEntryDateValue != null || maxEntryDateValue != null) {
                    DatePickerDialog(
                        labelID = R.string.min_entry_date,
                        date = minEntryDateValue,
                        datePickerState = datePickerState,
                        onDateSelected = {
                            minEntryDateValue = it
                        }
                    )
                    Spacer(modifier = Modifier.height(5.dp))

                    DatePickerDialog(
                        labelID = R.string.max_entry_date,
                        date = maxEntryDateValue,
                        datePickerState = datePickerState,
                        onDateSelected = {
                            maxEntryDateValue = it
                        }
                    )
                }

            }

            item {
                ExpandableSection(stringResource(R.string.sold_date),
                    expandedP = minSoldDateValue != null || maxSoldDateValue != null) {
                    DatePickerDialog(
                        labelID = R.string.min_sold_date,
                        date = minSoldDateValue,
                        datePickerState = datePickerState,
                        onDateSelected = {
                            minSoldDateValue = it
                        }
                    )

                    DatePickerDialog(
                        labelID = R.string.max_sold_date,
                        date = maxSoldDateValue,
                        datePickerState = datePickerState,
                        onDateSelected = {
                            maxSoldDateValue = it
                        }
                    )
                }

            }

            item {
                ExpandableSection(
                    stringResource(R.string.amenities),
                    expandedP = selectedAmenities.isNotEmpty()
                ) {
                    SelectableChipsGroup(
                        selectedOptions = selectedAmenities,
                        onSelectionChanged = { selectedAmenities = it }
                    )
                }

            }

            item {
                ExpandableSection(stringResource(R.string.agent),
                    expandedP = selectedAgent != null) {
                    AgentDropdown(
                        agents = agents,
                        selectedAgent = selectedAgent,
                        onAgentSelected = { selectedAgent = it },
                        isForSearch = true
                    )
                }

            }

            item {
                ExpandableSection(title = stringResource(R.string.number_of_rooms),
                    expandedP = minNumberOfRooms != null || maxNumberOfRooms != null) {
                    NumberOfRooms(
                        labelID = R.string.min_number_of_rooms,
                        value = minNumberOfRooms?.toString() ?: ""
                    ) {
                        minNumberOfRooms = it.toIntOrNull()
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    NumberOfRooms(
                        labelID = R.string.max_number_of_rooms,
                        value = maxNumberOfRooms?.toString() ?: ""
                    ) {
                        maxNumberOfRooms = it.toIntOrNull()
                    }
                }

            }

        }
    }
}

@Composable
fun NumberOfRooms(labelID: Int, value: String, onValueChanged: (String) -> Unit) {

    ThemeOutlinedTextField(
        value = value,
        onValueChanged = { onValueChanged(it) },
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Number,
        labelID = labelID,
        modifier = Modifier.fillMaxWidth()
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    labelID: Int,
    date: Date?,
    datePickerState: DatePickerState,
    onDateSelected: (Date?) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    val selectedDateText = date?.let {
        val localDate = it.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        "${localDate.dayOfMonth}/${localDate.monthValue}/${localDate.year}"
    } ?: ""

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val selectedDate = Date(it)
                        onDateSelected(selectedDate)
                    }
                    showDialog = false
                }) {
                    ThemeText(text = stringResource(R.string.ok), style = ThemeTextStyle.NORMAL)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = stringResource(R.string.cancel))
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
            .height(56.dp),
        onClick = { showDialog = true },
        onClear = { onDateSelected(null) }, // nouvelle action de suppression
        isClearable = date != null
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
    isEuro: Boolean
) {
    val currencyString = Utils().getCorrectPriceComponent(0, isEuro).currency
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        ThemeOutlinedTextField(
            value = minPriceValue?.toString() ?: "",
            onValueChanged = { onMinPriceChange(it.toIntOrNull()) },
            labelID = R.string.min_price,
            imeAction = ImeAction.Next,
            iconText = currencyString,
            keyboardType = KeyboardType.Number,
            modifier = Modifier.weight(1f)
        )

        ThemeOutlinedTextField(
            value = maxPriceValue?.toString() ?: "",
            onValueChanged = { onMaxPriceChange(it.toIntOrNull()) },
            labelID = R.string.max_price,
            imeAction = ImeAction.Next,
            iconText = currencyString,
            keyboardType = KeyboardType.Number,
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
    Column(
        modifier = Modifier
            .heightIn(max = 200.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ThemeText(
            text = stringResource(R.string.realty_type),
            style = ThemeTextStyle.NORMAL
        )
        RealtyType.entries.forEach { realtyType ->
            val isChecked = realtyType in selectedTypes
            RealtyTypeItem(
                realtyTypeName = realtyType.name,
                isChecked = isChecked,
                onCheckedChange = { checked ->
                    val updatedList = if (checked) {
                        selectedTypes + realtyType
                    } else {
                        selectedTypes - realtyType
                    }
                    onSelectionChanged(updatedList)
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


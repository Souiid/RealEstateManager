package com.openclassrooms.realestatemanager.ui.screens.search

import android.icu.util.Currency
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.RealtyType
import com.openclassrooms.realestatemanager.ui.composable.PriceTextField
import com.openclassrooms.realestatemanager.ui.composable.ThemeOutlinedTextField

@Composable
fun SearchScreen() {
    var selectedList by remember { mutableStateOf(emptyList<RealtyType>()) }
    var selectedStatus by remember { mutableStateOf<Boolean?>(null) }
    var minPriceValue by remember { mutableStateOf<Int?>(null) }
    var maxPriceValue by remember { mutableStateOf<Int?>(null) }
    val currency = "$"

    var minSurfaceValue by remember { mutableStateOf<Int?>(null) }
    var maxSurfaceValue by remember { mutableStateOf<Int?>(null) }

    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        item {
            StatusSegmentedButton(
                selectedStatus = selectedStatus,
                onStatusSelected = { selectedStatus = it }
            )
            HorizontalDivider()
        }

        item {
            RealtyTypeSelection(
                onSelectionChanged = { selectedList = it }
            )
            HorizontalDivider()
        }

        item {
            SetPriceFilterTextFields(
                minPriceValue = minPriceValue,
                maxPriceValue = maxPriceValue,
                onMinPriceChange = { minPriceValue = it},
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

    }

}

@Composable
fun SetSurfaceFilterTextFields(minSurfaceValue: Int?,
                             maxSurfaceValue: Int?,
                             onMinSurfaceChange: (Int?) -> Unit,
                             onMaxSurfaceChange: (Int?) -> Unit) {


    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        ThemeOutlinedTextField(
            value = minSurfaceValue.toString(),
            onValueChanged = { onMinSurfaceChange(it.toIntOrNull()) },
            labelID = R.string.min_surface,
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number,
            modifier = Modifier.weight(1f)
        )

        ThemeOutlinedTextField(
            value = maxSurfaceValue.toString(),
            onValueChanged = { onMaxSurfaceChange(it.toIntOrNull()) },
            labelID = R.string.max_surface,
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number,
            modifier = Modifier.weight(1f)
        )

    }
}

@Composable
fun SetPriceFilterTextFields(minPriceValue: Int?,
                             maxPriceValue: Int?,
                             onMinPriceChange: (Int?) -> Unit,
                             onMaxPriceChange: (Int?) -> Unit,
                             currencyP: String) {

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
        "Tous" to null,
        "Disponible" to true,
        "En vente" to false
    )

    val selectedIndex = statusList.indexOfFirst { it.second == selectedStatus }

    Text("Statut du bien")

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
    onSelectionChanged: (List<RealtyType>) -> Unit
) {
    val checkList =
        remember { mutableStateListOf(*List(RealtyType.entries.size) { false }.toTypedArray()) }

    Column(
        modifier = Modifier
            .heightIn(max = 200.dp) // hauteur max auto-adaptée
            .verticalScroll(rememberScrollState())
    ) {
        Text("Type de bien")
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


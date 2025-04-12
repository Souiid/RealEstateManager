package com.openclassrooms.realestatemanager.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun PriceTextField(
    value: String?,
    onValueChange: (String) -> Unit,
    labelID: Int,
    currency: String,
    onCurrencyChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val currencies = listOf("€", "$")

    ThemeOutlinedTextField(
        value = if (value == null || value == "null") "" else value,
        onValueChanged = onValueChange,
        labelID = labelID,
        imeAction = ImeAction.Next,
        keyboardType = KeyboardType.Number,
        modifier = modifier,
        trailingIcon = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                VerticalDivider()
                Spacer(modifier = Modifier.width(8.dp))
                DropdownMenuBox(
                    selectedText = currency,
                    options = currencies,
                    onOptionSelected = onCurrencyChange
                )
            }
        }
    )
}

@Composable
fun DropdownMenuBox(
    selectedText: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Text(
            text = selectedText,
            modifier = Modifier
                .clickable { expanded = true }
                .padding(horizontal = 8.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
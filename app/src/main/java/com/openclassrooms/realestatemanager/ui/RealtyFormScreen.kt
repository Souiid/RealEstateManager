package com.openclassrooms.realestatemanager.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.RealtyType
import com.openclassrooms.realestatemanager.ui.composable.BaseScreen
import com.openclassrooms.realestatemanager.ui.composable.ThemeButton
import com.openclassrooms.realestatemanager.ui.composable.ThemeOutlinedTextField
import com.openclassrooms.realestatemanager.ui.composable.ThemeTopBar

class RealtyFormScreen : BaseScreen<RealtyFormScreen.Params>() {

    private var onNext: () -> Unit = {}

    data class Params(
        val onNext: () -> Unit
    )

    override fun setParameters(params: Params) {
        params.let {
            onNext = it.onNext
        }
    }

    @Composable
    override fun InitViewModel() {

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun ScreenInternal() {
        Text("Realty Form Screen")

        val surfaceValue = remember { mutableStateOf("") }
        val priceValue = remember { mutableStateOf("") }
        val numberOfRoomsValue = remember { mutableStateOf("") }
        val descriptionValue = remember { mutableStateOf("") }

        val options =
            RealtyType.entries.map { it -> it.name.lowercase().replaceFirstChar { it.uppercase() } }
        var expanded by remember { mutableStateOf(false) }
        var selectedOption by remember {
            mutableStateOf(options[0])
        }

        Scaffold(modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
            topBar = { ThemeTopBar(title = "Realty form", onBackClick = { }) },
            bottomBar = {
                ThemeButton(
                    onClick = {
                        onNext()
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
            }
        }
    }

    override fun screenLifeCycle(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event) {

    }
}
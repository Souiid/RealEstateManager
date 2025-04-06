package com.openclassrooms.realestatemanager.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import com.openclassrooms.realestatemanager.ui.composable.ThemeButton
import com.openclassrooms.realestatemanager.ui.composable.ThemeOutlinedTextField
import com.openclassrooms.realestatemanager.ui.composable.ThemeTopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectAgentScreen(viewModel: SelectAgentViewModel, onBack: () -> Unit, onFinish: ()->Unit) {

    val lifecycleOwner = LocalLifecycleOwner.current
    var expanded by remember { mutableStateOf(false) }
    var displayTF by remember { mutableStateOf(false) }
    var agentName by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val agents by viewModel.agentsFlow.collectAsState(initial = emptyList())
    var selectedAgent by remember { mutableStateOf<RealtyAgent?>(null) }
    val realtyPrimaryInfo = viewModel.getRealtyPrimaryInfo()
    val realtyPictures = viewModel.getImages()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            ThemeTopBar(title = "Select agent", onBackClick = { onBack() })
        },
        bottomBar = {
            ThemeButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                text = "Next",
                enabled = true,
                onClick = {
                    val realty = Realty(agentId = selectedAgent!!.id,
                        primaryInfo = realtyPrimaryInfo!!,
                        pictures = realtyPictures!!)
                    scope.launch {
                        viewModel.insertRealty(realty)
                        onFinish()
                    }

                }
            )
        },

        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (!displayTF) {
                    if (agents.isNotEmpty()) {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = selectedAgent?.name ?: agents.first().name,
                                onValueChange = {},
                                label = {
                                    Text(text = "Agent")
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
                                agents.forEach { agent ->
                                    DropdownMenuItem(
                                        text = { Text(agent.name) },
                                        onClick = {
                                            selectedAgent = agent
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    } else {
                        Text("You have no agent yet, please add one")
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    ThemeButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = if (agents.isNotEmpty()) "Or add agent" else "Add agent",
                        enabled = true,
                        onClick = { displayTF = true }
                    )
                } else {
                    ThemeOutlinedTextField(
                        value = agentName,
                        labelID = R.string.agent_name,
                        imeAction = ImeAction.Done,
                        iconText = null,
                        keyboardType = KeyboardType.Text,
                        onValueChanged = { agentName = it },

                        )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        ThemeButton(
                            modifier = Modifier.weight(1f),
                            text = "Cancel",
                            enabled = true,
                            onClick = { displayTF = false }
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        ThemeButton(
                            modifier = Modifier.weight(1f),
                            text = "Add",
                            enabled = true,
                            onClick = {
                                scope.launch {
                                    viewModel.insertAgent(agentName)
                                    displayTF = false
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}
package com.openclassrooms.realestatemanager.ui.screens.form.selectagent

import android.widget.Toast
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
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.ui.composable.ThemeText
import com.openclassrooms.realestatemanager.ui.composable.ThemeTextStyle
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import com.openclassrooms.realestatemanager.ui.composable.AgentDropdown
import com.openclassrooms.realestatemanager.ui.composable.CheckAnimation
import com.openclassrooms.realestatemanager.ui.composable.ThemeButton
import com.openclassrooms.realestatemanager.ui.composable.ThemeOutlinedTextField
import com.openclassrooms.realestatemanager.ui.composable.ThemeTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SelectAgentScreen(
    selectAgentViewModel: SelectAgentViewModel = koinViewModel(),
    onBack: () -> Unit, onFinish: () -> Unit
) {

    val context = LocalContext.current
    var displayTF by remember { mutableStateOf(false) }
    var agentName by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val agents by selectAgentViewModel.agentsFlow.collectAsState(initial = emptyList())
    var selectedAgent by remember { mutableStateOf<RealtyAgent?>(null) }
    val realtyPrimaryInfo = selectAgentViewModel.getRealtyPrimaryInfo()
    val realtyPictures = selectAgentViewModel.getImages()
    var isFinish by remember { mutableStateOf(false) }
    var showNameError by remember { mutableStateOf(false) }

    LaunchedEffect(isFinish) {
        if (isFinish) {
            delay(2000)
            onFinish()
        }
    }

    LaunchedEffect(agents) {
        if (agents.isNotEmpty()) {
            selectedAgent = agents[0]
        }
    }

    if (showNameError) {
        Toast.makeText(
            context,
            context.getString(R.string.agent_must_have_two_characters),
            Toast.LENGTH_SHORT
        ).show()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            ThemeTopBar(title = stringResource(R.string.select_agent), onBackClick = { onBack() })
        },
        bottomBar = {
            if (!displayTF) {
                if (!isFinish) {
                    ThemeButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        text = stringResource(R.string.complete),
                        enabled = selectedAgent != null,
                        onClick = {
                            val realty = Realty(
                                agentId = selectedAgent!!.id,
                                primaryInfo = realtyPrimaryInfo!!,
                                pictures = realtyPictures!!,
                            )
                            scope.launch {
                                selectAgentViewModel.insertRealty(realty)
                                isFinish = true
                            }

                        }
                    )
                }
            }


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
                if (isFinish) {
                    CheckAnimation()
                    Spacer(Modifier.height(20.dp))
                    ThemeText(
                        text = stringResource(R.string.realty_added),
                        style = ThemeTextStyle.NORMAL
                    )
                } else {
                    if (!displayTF) {
                        if (agents.isNotEmpty()) {
                            AgentDropdown(
                                agents = agents,
                                selectedAgent = selectedAgent,
                                onAgentSelected = { selectedAgent = it }
                            )
                        } else {
                            Text(stringResource(R.string.no_agent))
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        ThemeButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = if (agents.isNotEmpty()) stringResource(R.string.or_add_agent) else stringResource(
                                R.string.add_agent
                            ),
                            enabled = true,
                            onClick = {
                                displayTF = true
                            }
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
                                text = stringResource(R.string.cancel),
                                enabled = true,
                                onClick = {
                                    displayTF = false
                                }
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            ThemeButton(
                                modifier = Modifier.weight(1f),
                                text = stringResource(R.string.add),
                                enabled = true,
                                onClick = {
                                    if (selectAgentViewModel.isAgentNameValid(agentName)) {
                                        scope.launch {
                                            selectAgentViewModel.insertAgent(agentName)
                                            agentName = ""
                                            displayTF = false
                                            showNameError = false
                                        }
                                    } else {
                                        showNameError = true
                                    }
                                }
                            )
                        }
                    }
                }

            }
        }
    )
}
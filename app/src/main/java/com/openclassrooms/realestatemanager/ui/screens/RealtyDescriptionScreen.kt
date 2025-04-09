package com.openclassrooms.realestatemanager.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import com.openclassrooms.realestatemanager.navigation.NavigationScreen
import kotlinx.coroutines.launch

@Composable
fun RealtyDescriptionScreen(
    viewModel: RealtyDescriptionViewModel,
    onBack: () -> Unit) {

    val realty = viewModel.getSelectedRealty()
    var realtyAgent by remember { mutableStateOf<RealtyAgent?>(null) }

    LaunchedEffect(Unit) {
        realtyAgent = viewModel.getAgentRepository(realty?.agentId ?: 0)
    }


    Scaffold(modifier = Modifier.fillMaxSize().padding(top = 40.dp)) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Text("Realty type: ${realty?.primaryInfo?.realtyType?.name}")


            Text("Realty agent ID: ${realty?.agentId}")
            Text("Realty Agent: ${realtyAgent?.name}")
        }
    }
}
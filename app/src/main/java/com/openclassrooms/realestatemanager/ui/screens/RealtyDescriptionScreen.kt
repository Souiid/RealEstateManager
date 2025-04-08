package com.openclassrooms.realestatemanager.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.navigation.NavigationScreen

@Composable
fun RealtyDescriptionScreen(
    viewModel: RealtyDescriptionViewModel,
    onBack: () -> Unit) {

    val realty = viewModel.getSelectedRealty()

    Scaffold(modifier = Modifier.fillMaxSize().padding(top = 40.dp)) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Text("Realty type: ${realty?.primaryInfo?.realtyType?.name}")
            Text("Realty agent ID: ${realty?.agentId}")
        }
    }
}
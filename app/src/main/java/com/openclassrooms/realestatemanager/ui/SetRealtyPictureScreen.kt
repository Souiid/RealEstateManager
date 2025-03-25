package com.openclassrooms.realestatemanager.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.openclassrooms.realestatemanager.ui.composable.ThemeTopBar

@Composable
fun SetRealtyPictureScreen(onBack: () -> Unit) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ThemeTopBar(
                title = "Realty pictures",
                onBackClick = { onBack() })
        }) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
        }
    }


}
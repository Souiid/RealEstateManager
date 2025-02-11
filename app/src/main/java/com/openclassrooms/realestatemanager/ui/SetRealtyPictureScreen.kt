package com.openclassrooms.realestatemanager.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.openclassrooms.realestatemanager.ui.composable.BaseScreen
import com.openclassrooms.realestatemanager.ui.composable.ThemeTopBar

class SetRealtyPictureScreen : BaseScreen<SetRealtyPictureScreen.Params>() {

    private var onBack: () -> Unit = {}
    private var onNext: () -> Unit = {}

    data class Params(
        val onNext: () -> Unit,
        val onBack: () -> Unit
    )

    override fun setParameters(params: Params) {
        params.let {
            onNext = it.onNext
            onBack = it.onBack
        }
    }

    @Composable
    override fun InitViewModel() {

    }

    @Composable
    override fun ScreenInternal() {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                ThemeTopBar(
                    title = "Realty pictures",
                    onBackClick = { onBack() })
            }) { innerPadding ->
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            }
        }
    }

    override fun screenLifeCycle(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event) {}
}
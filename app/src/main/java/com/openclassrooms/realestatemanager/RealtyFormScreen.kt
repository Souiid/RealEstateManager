package com.openclassrooms.realestatemanager

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.openclassrooms.realestatemanager.composable.BaseScreen

class RealtyFormScreen: BaseScreen<RealtyFormScreen.Params>() {

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

    @Composable
    override fun ScreenInternal() {
       Text("Realty Form Screen")
    }

    override fun screenLifeCycle(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event) {

    }
}
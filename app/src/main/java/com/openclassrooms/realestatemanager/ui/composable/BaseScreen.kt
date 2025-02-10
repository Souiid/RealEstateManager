package com.openclassrooms.realestatemanager.ui.composable

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.openclassrooms.realestatemanager.navigation.NavigationScreen


abstract class BaseScreen<in P> {
    var navigationScreen: NavigationScreen? = null
    var activity: Activity? = null

    @Composable
    fun Screen(params: P, navigationScreen: NavigationScreen, activity: Activity) {
        this.navigationScreen = navigationScreen
        this.activity = activity
        setParameters(params)
        InitViewModel()
        ScreenInternal()
        ComposableLifecycle { source, event ->
            screenLifeCycle(source, event)
        }
    }

    abstract fun setParameters(params: P)
    @Composable
    protected abstract fun InitViewModel()
    @Composable
    protected abstract fun ScreenInternal()
    protected abstract fun screenLifeCycle(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event)
}
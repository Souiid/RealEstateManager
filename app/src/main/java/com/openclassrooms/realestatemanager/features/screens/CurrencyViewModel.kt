package com.openclassrooms.realestatemanager.features.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.DataStoreManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CurrencyViewModel(
    private val context: Context,
): ViewModel() {
    val isEuroFlow: StateFlow<Boolean> = DataStoreManager.getIsEuro(context)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    fun setIsEuro(enabled: Boolean) {
        viewModelScope.launch {
            DataStoreManager.setIsEuro(context, enabled)
        }
    }
}
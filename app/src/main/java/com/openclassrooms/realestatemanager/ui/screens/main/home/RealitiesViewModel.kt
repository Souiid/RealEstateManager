package com.openclassrooms.realestatemanager.ui.screens.main.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.SearchCriteria
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import com.openclassrooms.realestatemanager.ui.screens.CurrencyViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class RealitiesViewModel(
    private val realtyRepository: IRealtyRepository,
    currencyViewModel: CurrencyViewModel,
    context: Context,
): ViewModel() {

    private val criteriaFlow: MutableStateFlow<SearchCriteria?> = MutableStateFlow(null)

    private val isEuroStateFlow = currencyViewModel.isEuroFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val realties: Flow<List<Realty>> = isEuroStateFlow.flatMapLatest { isEuro ->
        criteriaFlow.flatMapLatest { criteria ->
            if (criteria == null) {
                realtyRepository.getAllRealties()
            } else {
                realtyRepository.getFilteredRealtiesFlow(criteria, isEuro)
            }
        }
    }

    private var placesClient: PlacesClient

    init {
        Places.initialize(context, context.getString(R.string.google_maps_key))
        placesClient = Places.createClient(context)
    }


    fun initRealtyRepository() {
        realtyRepository.setSelectedRealty(null)
        realtyRepository.updatedRealty = null
    }

    fun setCriteria(criteria: SearchCriteria?) {
        criteriaFlow.value = criteria
    }

}
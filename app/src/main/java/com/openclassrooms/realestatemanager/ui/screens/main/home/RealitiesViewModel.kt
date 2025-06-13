package com.openclassrooms.realestatemanager.ui.screens.main.home

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.SearchCriteria
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class RealitiesViewModel(
    private val realtyRepository: IRealtyRepository,
    context: Context,
): ViewModel() {

    private val criteriaFlow: MutableStateFlow<SearchCriteria?> = MutableStateFlow(null)
    val realties: Flow<List<Realty>> = criteriaFlow.flatMapLatest { criteria ->
        if (criteria == null) {
            realtyRepository.getAllRealties()
        } else {
            realtyRepository.getFilteredRealtiesFlow(criteria)
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
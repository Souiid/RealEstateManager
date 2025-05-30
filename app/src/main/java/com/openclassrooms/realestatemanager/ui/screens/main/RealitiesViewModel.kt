package com.openclassrooms.realestatemanager.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.openclassrooms.realestatemanager.data.SearchCriteria
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class RealitiesViewModel(
    private val realtyRepository: IRealtyRepository
): ViewModel() {

    private val criteriaFlow: MutableStateFlow<SearchCriteria?> = MutableStateFlow(null)
    val realties: Flow<List<Realty>> = criteriaFlow.flatMapLatest { criteria ->
        if (criteria == null) {
            realtyRepository.getAllRealties()
        } else {
            realtyRepository.getFilteredRealtiesFlow(criteria)
        }
    }


    fun initRealtyRepository() {
        realtyRepository.setSelectedRealty(null)
        realtyRepository.updatedRealty = null
    }

    fun getFilteredRealties(): List<Realty> {
        return realtyRepository.filteredRealties
    }

    fun setCriteria(criteria: SearchCriteria?) {
        criteriaFlow.value = criteria
    }

    fun setSelectedRealty(realty: Realty) {
        realtyRepository.setSelectedRealty(realty)
    }


}
package com.openclassrooms.realestatemanager.ui.screens

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.data.room.entities.Realty

class RealtyDescriptionViewModel(private val realtyRepository: IRealtyRepository) : ViewModel() {

    fun getSelectedRealty(): Realty? {
        return realtyRepository.selectedRealty
    }

}
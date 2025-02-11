package com.openclassrooms.realestatemanager.ui

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.data.INewRealtyRepository
import com.openclassrooms.realestatemanager.data.RealtyPlace
import com.openclassrooms.realestatemanager.data.RealtyPrimaryInfo

class RealtyFormViewModel(private val repository: INewRealtyRepository) : ViewModel() {

    fun setPrimaryInfo(
       realtyPrimaryInfo: RealtyPrimaryInfo
    ) {
        repository.realtyPrimaryInfo = realtyPrimaryInfo
    }

    fun isFormValid(
        surface: String,
        price: String,
        rooms: String,
        description: String,
        realtyPlace: RealtyPlace?
    ): Boolean {
        return surface.isNotBlank() && price.isNotBlank() && rooms.isNotBlank() && description.isNotBlank() && realtyPlace != null
    }

    fun getPrimaryInfo(): RealtyPrimaryInfo? {
        return repository.realtyPrimaryInfo
    }

}
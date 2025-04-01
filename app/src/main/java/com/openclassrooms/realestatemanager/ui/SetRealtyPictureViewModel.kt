package com.openclassrooms.realestatemanager.ui

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.data.INewRealtyRepository
import com.openclassrooms.realestatemanager.data.RealtyPicture

class SetRealtyPictureViewModel(val repository: INewRealtyRepository): ViewModel() {

    fun setRealtyPictures(realtyPictures: List<RealtyPicture>) {
        repository.images = realtyPictures
    }

    fun getRealtyPictures(): List<RealtyPicture>? {
        return repository.images
    }
}
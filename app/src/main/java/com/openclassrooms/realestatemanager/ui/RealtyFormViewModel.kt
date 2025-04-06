package com.openclassrooms.realestatemanager.ui

import androidx.lifecycle.ViewModel
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.openclassrooms.realestatemanager.data.repositories.INewRealtyRepository
import com.openclassrooms.realestatemanager.data.RealtyPlace
import com.openclassrooms.realestatemanager.data.RealtyPrimaryInfo
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import kotlinx.coroutines.flow.Flow

class RealtyFormViewModel(
    private val repository: INewRealtyRepository
) : ViewModel() {

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

    fun searchPlaces(
        placesClient: PlacesClient,
        query: String,
        onResults: (List<AutocompletePrediction>) -> Unit
    ) {
        if (query.isEmpty()) {
            onResults(emptyList())
            return
        }

        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                val predictions = response.autocompletePredictions
                onResults(predictions)
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                onResults(emptyList())
            }
    }

}
package com.openclassrooms.realestatemanager.ui.screens.form.realtyform

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.repositories.INewRealtyRepository
import com.openclassrooms.realestatemanager.data.RealtyPlace
import com.openclassrooms.realestatemanager.data.RealtyPrimaryInfo
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class RealtyFormViewModel(
    private val newRealtyRepository: INewRealtyRepository,
    private val realtyRepository: IRealtyRepository
) : ViewModel() {

    fun setPrimaryInfo(
        realtyPrimaryInfo: RealtyPrimaryInfo,
        updatedRealty: Realty? = null,
    ) {
        if (updatedRealty == null) {
            newRealtyRepository.realtyPrimaryInfo = realtyPrimaryInfo
        } else {
            updatedRealty.primaryInfo = realtyPrimaryInfo
            realtyRepository.updatedRealty = updatedRealty
        }
    }

    fun getFormValidationError(
        surface: String,
        price: String,
        rooms: String,
        bedrooms: String,
        bathrooms: String,
        description: String,
        realtyPlace: RealtyPlace?,
        resources: Resources
    ): String? {
        return when {
            surface.isBlank() -> resources.getString(R.string.error_surface_required)
            price.isBlank() -> resources.getString(R.string.error_price_required)
            rooms.isBlank() -> resources.getString(R.string.error_rooms_required)
            bedrooms.isBlank() -> resources.getString(R.string.error_bedrooms_required)
            bathrooms.isBlank() -> resources.getString(R.string.error_bathrooms_required)
            description.isBlank() -> resources.getString(R.string.error_description_required)
            realtyPlace == null -> resources.getString(R.string.error_place_required)
            !isNumberOfRoomsValid(rooms.toIntOrNull() ?: 0, bedrooms.toIntOrNull() ?: 0, bathrooms.toIntOrNull() ?: 0) ->
                resources.getString(R.string.error_rooms_logic)
            else -> null
        }
    }

    private fun isNumberOfRoomsValid(rooms: Int, bedrooms: Int, bathrooms: Int): Boolean = bedrooms + bathrooms + 1 <= rooms

    fun getPrimaryInfo(): RealtyPrimaryInfo? {
        return newRealtyRepository.realtyPrimaryInfo
    }

    fun getRealtyFromRealtyRepository(): Realty? {
        return realtyRepository.updatedRealty ?: realtyRepository.selectedRealtyFlow.value

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

    suspend fun fetchPlaceLatLng(
        placesClient: PlacesClient,
        placeId: String
    ): LatLng? = suspendCancellableCoroutine { continuation ->

        val request = FetchPlaceRequest.builder(
            placeId,
            listOf(Place.Field.LOCATION)
        ).build()

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                val latitude = response.place.location?.latitude ?: return@addOnSuccessListener
                val longitude = response.place.location?.longitude ?: return@addOnSuccessListener
                val latLng = LatLng(latitude, longitude)
                continuation.resume(latLng)
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                continuation.resume(null)
            }
    }

}
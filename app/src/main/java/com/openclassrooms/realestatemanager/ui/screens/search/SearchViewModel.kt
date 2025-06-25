package com.openclassrooms.realestatemanager.ui.screens.search

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.SearchCriteria
import com.openclassrooms.realestatemanager.data.repositories.IAgentRepository
import com.openclassrooms.realestatemanager.data.repositories.ISearchRepository
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Date
import kotlin.coroutines.resume

class SearchViewModel(
    agentRepository: IAgentRepository,
    private val searchRepository: ISearchRepository
) : ViewModel() {

    val agentsFlow: Flow<List<RealtyAgent>> = agentRepository.getAllAgents()
    private val _criteriaFlow = MutableStateFlow<SearchCriteria?>(null)

    val criteriaFlow: StateFlow<SearchCriteria?> = _criteriaFlow

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

    fun validateCriteria(
        context: Context,
        minPrice: Int?, maxPrice: Int?,
        minSurface: Int?, maxSurface: Int?,
        minRooms: Int?, maxRooms: Int?,
        minEntryDate: Date?, maxEntryDate: Date?,
        minSoldDate: Date?, maxSoldDate: Date?
    ): String? {
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            return context.getString(R.string.error_price_range)
        }

        if (minSurface != null && maxSurface != null && minSurface > maxSurface) {
            return context.getString(R.string.error_surface_range)
        }

        if (minRooms != null && maxRooms != null && minRooms > maxRooms) {
            return context.getString(R.string.error_room_range)
        }

        if (minEntryDate != null && maxEntryDate != null && minEntryDate.after(maxEntryDate)) {
            return context.getString(R.string.error_entry_date_range)
        }

        if (minSoldDate != null && maxSoldDate != null && minSoldDate.after(maxSoldDate)) {
            return context.getString(R.string.error_sold_date_range)
        }

        return null
    }

    fun setCriteria(criteria: SearchCriteria) {
        searchRepository.saveCriteria(criteria)
        _criteriaFlow.value = criteria
    }
}
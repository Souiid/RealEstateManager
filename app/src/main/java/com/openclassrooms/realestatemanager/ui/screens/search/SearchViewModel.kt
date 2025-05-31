package com.openclassrooms.realestatemanager.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.openclassrooms.realestatemanager.data.RealtyPlace
import com.openclassrooms.realestatemanager.data.SearchCriteria
import com.openclassrooms.realestatemanager.data.Utils
import com.openclassrooms.realestatemanager.data.repositories.IAgentRepository
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.data.repositories.ISearchRepository
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Date
import kotlin.coroutines.resume
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class SearchViewModel(
    private val realtyRepository: IRealtyRepository,
    private val agentRepository: IAgentRepository,
    private val searchRepository: ISearchRepository
    ): ViewModel() {

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

    fun setCriteria(criteria: SearchCriteria) {
        searchRepository.saveCriteria(criteria)
        _criteriaFlow.value = criteria
    }


    fun getCriteria(): SearchCriteria? = _criteriaFlow.value

    fun calculateDistanceInKm(start: LatLng, end: LatLng): Double {
        val earthRadiusKm = 6371.0

        val dLat = Math.toRadians(end.latitude - start.latitude)
        val dLon = Math.toRadians(end.longitude - start.longitude)

        val lat1 = Math.toRadians(start.latitude)
        val lat2 = Math.toRadians(end.latitude)

        val a = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(lat1) * cos(lat2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadiusKm * c
    }

    fun filterRealtyWithinRadius(
        allRealty: List<RealtyPlace>,
        center: LatLng,
        radiusKm: Double
    ): List<RealtyPlace> {
        return allRealty.filter { realty ->
            val distance = calculateDistanceInKm(center, realty.positionLatLng)
            distance <= radiusKm
        }
    }

   // fun setFilteredSearch(
   //     isAvailable: Boolean? = null,
   //     minPrice: Double? = null,
   //     maxPrice: Double? = null,
   //     minSurface: Double? = null,
   //     maxSurface: Double? = null,
   //     minRooms: Int? = null,
   //     maxRooms: Int? = null,
   //     minEntryDate: Date? = null,
   //     maxEntryDate: Date? = null,
   //     minSoldDate: Date? = null,
   //     maxSoldDate: Date? = null,
   //     realtyTypes: List<String>? = null,
   //     amenity: String? = null,
   //     isReset: Boolean = false
   // ) {
   //     viewModelScope.launch {
   //         realtyRepository.setFilteredRealties(
   //             isAvailable,
   //             minPrice,
   //             maxPrice,
   //             minSurface,
   //             maxSurface,
   //             minRooms,
   //             maxRooms,
   //             minEntryDate,
   //             maxEntryDate,
   //             minSoldDate,
   //             maxSoldDate,
   //             realtyTypes,
   //             amenity,
   //             isReset
   //         )
   //     }
   // }
}
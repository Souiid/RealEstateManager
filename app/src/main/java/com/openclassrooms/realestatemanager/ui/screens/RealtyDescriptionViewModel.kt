package com.openclassrooms.realestatemanager.ui.screens

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.repositories.IAgentRepository
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class RealtyDescriptionViewModel(
    context: Context,
    private val realtyRepository: IRealtyRepository,
    private val agentRepository: IAgentRepository
) : ViewModel() {

    private var placesClient: PlacesClient

    init {
        Places.initialize(context, context.getString(R.string.google_maps_key))
         placesClient = Places.createClient(context)
    }


    fun getSelectedRealty(): Realty? {
        return realtyRepository.selectedRealty
    }

    suspend fun getAgentRepository(agentId: Int): RealtyAgent? {
        return agentRepository.getAgentByID(agentId)
    }

    suspend fun fetchPlaceLatLng(
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

    fun uriToBitmapLegacy(context: Context, uri: Uri): Bitmap? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
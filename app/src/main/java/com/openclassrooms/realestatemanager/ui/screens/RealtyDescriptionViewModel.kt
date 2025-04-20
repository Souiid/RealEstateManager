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
import com.openclassrooms.realestatemanager.data.Utils
import com.openclassrooms.realestatemanager.data.repositories.IAgentRepository
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class RealtyDescriptionViewModel(
    context: Context,
    private val realtyRepository: IRealtyRepository,
    private val agentRepository: IAgentRepository
) : ViewModel() {

    private var placesClient: PlacesClient

    val selectedRealty: StateFlow<Realty?> = realtyRepository.selectedRealtyFlow

    init {
        Places.initialize(context, context.getString(R.string.google_maps_key))
         placesClient = Places.createClient(context)
    }

    suspend fun getAgentRepository(agentId: Int): RealtyAgent? {
        return agentRepository.getAgentByID(agentId)
    }
    fun uriToBitmapLegacy(context: Context, uri: Uri): Bitmap? {
       return Utils().uriToBitmapLegacy(context, uri)
    }

}
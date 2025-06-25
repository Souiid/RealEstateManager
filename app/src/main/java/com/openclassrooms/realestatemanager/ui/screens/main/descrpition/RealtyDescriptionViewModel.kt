package com.openclassrooms.realestatemanager.ui.screens.main.descrpition

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.Utils
import com.openclassrooms.realestatemanager.data.repositories.IAgentRepository
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

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

    fun getTodayDate(date: Date): String {
        return Utils().getTodayDate(date)
    }

    fun getRealtyFromID(realtyID: Int) {
        viewModelScope.launch {
            val realty = realtyRepository.getRealtyFromID(realtyID)
            realtyRepository.setSelectedRealty(realty)
        }
    }

    fun updateRealtyStatus(realty: Realty) {
        viewModelScope.launch {
            realtyRepository.updateRealty(realty)
            realtyRepository.setSelectedRealty(realty) // <- ajoute ça

        }
    }

    fun setSelectedRealty(realty: Realty?) {
        realtyRepository.setSelectedRealty(realty)
    }

}
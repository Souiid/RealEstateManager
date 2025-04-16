package com.openclassrooms.realestatemanager.ui.screens.map

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository

class MapViewModel(
    context: Context,
    private val realtyRepository: IRealtyRepository
) : ViewModel() {

    private var placesClient: PlacesClient

    init {
        Places.initialize(context, context.getString(R.string.google_maps_key))
        placesClient = Places.createClient(context)
    }

    //fun getSortedRealities(): List<Realty>? {
      //  return realtyRepository.sortedRealities
    //}

    //suspend fun fetchPlaceLatLng(realty: Realty): LatLng? {
       // return Utils().fetchPlaceLatLng(placesClient, realty.primaryInfo.realtyPlace.id)
    //}
}
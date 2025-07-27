package com.openclassrooms.realestatemanager.viewmodel

import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.data.*
import com.openclassrooms.realestatemanager.data.room.Amenity
import com.openclassrooms.realestatemanager.data.room.entities.Realty

class Utils {

    fun createRealty(): Realty {
        return Realty(
            id = 1,
            agentId = 1,
            primaryInfo = createPrimaryInfo(),
            entryDate = java.util.Date(),
            pictures = emptyList()
        )
    }

    fun createPrimaryInfo(): RealtyPrimaryInfo {
        return RealtyPrimaryInfo(
            realtyType = RealtyType.HOUSE,
            surface = 100,
            price = 200000,
            roomsNbr = 4,
            bathroomsNbr = 1,
            bedroomsNbr = 2,
            description = "Test",
            realtyPlace = RealtyPlace("id", "place", LatLng(0.0, 0.0)),
            amenities = emptyList(),
            isEuro = true
        )
    }

    fun createSearchCriteria(
        minPrice: Int? = null,
        maxPrice: Int? = null,
        amenities: List<Amenity> = emptyList(),
        realtyTypes: List<RealtyType> = emptyList(),
        radiusKm: Double? = null,
        centerLatLng: LatLng? = null
    ): SearchCriteria {
        return SearchCriteria(
            minPrice = minPrice,
            maxPrice = maxPrice,
            minSurface = null,
            maxSurface = null,
            minRooms = null,
            maxRooms = null,
            isAvailable = null,
            minEntryDate = null,
            maxEntryDate = null,
            minSoldDate = null,
            maxSoldDate = null,
            amenities = amenities,
            realtyTypes = realtyTypes,
            selectedAgent = null,
            radiusKm = radiusKm,
            centerPlace = centerLatLng?.let { RealtyPlace("center", "Center", it) }
        )
    }

    fun createRealtyWithPrice(id: Int, price: Int, isEuro: Boolean = true): Realty {
        val base = createRealty()
        return base.copy(
            id = id,
            primaryInfo = base.primaryInfo.copy(price = price, isEuro = isEuro)
        )
    }

    fun createFilteredRealties(vararg realties: Realty): List<Realty> = realties.toList()
}
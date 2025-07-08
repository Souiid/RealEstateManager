package com.openclassrooms.realestatemanager.viewmodel

import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.data.RealtyPlace
import com.openclassrooms.realestatemanager.data.RealtyPrimaryInfo
import com.openclassrooms.realestatemanager.data.RealtyType
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import java.util.Date

class Utils {

    fun createRealty(): Realty {
        return Realty(
            id = 1,
            agentId = 1,
            primaryInfo = createPrimaryInfo(),
            entryDate = Date(),
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
}
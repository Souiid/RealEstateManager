package com.openclassrooms.realestatemanager.data

import com.openclassrooms.realestatemanager.data.room.Amenity

data class RealtyPrimaryInfo(
    var realtyType: RealtyType,
    var surface: Double,
    var price: Double,
    var roomsNbr: Int,
    var bathroomsNbr: Int,
    var bedroomsNbr: Int,
    var description: String,
    var realtyPlace: RealtyPlace,
    var amenities: List<Amenity> = emptyList()
)

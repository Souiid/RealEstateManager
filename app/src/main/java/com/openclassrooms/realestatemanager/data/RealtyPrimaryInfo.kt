package com.openclassrooms.realestatemanager.data

data class RealtyPrimaryInfo(
    var realtyType: RealtyType,
    var surface: Double,
    var price: Double,
    var roomsNbr: Int,
    var bathroomsNbr: Int,
    var bedroomsNbr: Int,
    var description: String,
    var realtyPlace: RealtyPlace
)

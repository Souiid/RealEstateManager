package com.openclassrooms.realestatemanager.data

data class RealtyPrimaryInfo(
    var realtyType: RealtyType,
    var surface: Double,
    var price: Double,
    var rooms: Int,
    var description: String,
    var realtyPlace: RealtyPlace
)

package com.openclassrooms.realestatemanager.data

data class Realty(
    val id: String,
    val realtyAgent: RealtyAgent,
    val realtyPrimaryInfo: RealtyPrimaryInfo,
    val realtyPicture: RealtyPicture
)
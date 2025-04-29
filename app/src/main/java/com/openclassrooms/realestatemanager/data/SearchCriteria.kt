package com.openclassrooms.realestatemanager.data

import com.openclassrooms.realestatemanager.data.room.Amenity
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import java.util.Date

data class SearchCriteria(
    val realtyTypes: List<RealtyType> = emptyList(),
    val isAvailable: Boolean? = null,
    val minPrice: Int? = null,
    val maxPrice: Int? = null,
    val minSurface: Int? = null,
    val maxSurface: Int? = null,
    val minEntryDate: Date? = null,
    val maxEntryDate: Date? = null,
    val minSoldDate: Date? = null,
    val maxSoldDate: Date? = null,
    val amenities: List<Amenity> = emptyList(),
    val selectedAgent: RealtyAgent? = null,
    val minRooms: Int? = null,
    val maxRooms: Int? = null,
)

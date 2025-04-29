package com.openclassrooms.realestatemanager.data.repositories

import com.openclassrooms.realestatemanager.data.room.entities.Realty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date

interface IRealtyRepository {
    var allRealties: List<Realty>
    val selectedRealtyFlow: StateFlow<Realty?>
    val sortedRealities: StateFlow<List<Realty>>
    var updatedRealty: Realty?
    fun setSelectedRealty(realty: Realty?)
    suspend fun insertRealty(realty: Realty)
    fun getAllRealties(): Flow<List<Realty>>
    suspend fun updateRealty(realty: Realty)
    suspend fun searchRealties(
        isAvailable: Boolean? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        minSurface: Double? = null,
        maxSurface: Double? = null,
        minRooms: Int? = null,
        entryDate: Date? = null,
        soldDate: Date? = null,
        realtyTypes: List<String>? = null,
        amenity: String? = null
    )
}
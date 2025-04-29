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
    suspend fun searchRealities(
        isAvailable: Boolean?,
        minPrice: Double?,
        maxPrice: Double?,
        minSurface: Double?,
        maxSurface: Double?,
        minRooms: Int?,
        maxRooms: Int?,
        minEntryDate: Date?,
        maxEntryDate: Date?,
        minSoldDate: Date?,
        maxSoldDate: Date?,
        realtyTypes: List<String>?,
        amenity: String?,
        isReset: Boolean
    )
}
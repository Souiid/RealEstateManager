package com.openclassrooms.realestatemanager.data.repositories

import com.openclassrooms.realestatemanager.data.room.entities.Realty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IRealtyRepository {
    val selectedRealtyFlow: StateFlow<Realty?>
    var sortedRealities: List<Realty>
    var updatedRealty: Realty?
    fun setSelectedRealty(realty: Realty?)
    suspend fun insertRealty(realty: Realty)
    fun getAllRealties(): Flow<List<Realty>>
    suspend fun updateRealty(realty: Realty)
}
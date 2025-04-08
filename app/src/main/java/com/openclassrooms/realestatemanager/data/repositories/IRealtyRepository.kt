package com.openclassrooms.realestatemanager.data.repositories

import com.openclassrooms.realestatemanager.data.room.entities.Realty
import kotlinx.coroutines.flow.Flow

interface IRealtyRepository {
    var selectedRealty: Realty?
    suspend fun insertRealty(realty: Realty)
    fun getAllRealties(): Flow<List<Realty>>
}
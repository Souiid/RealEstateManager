package com.openclassrooms.realestatemanager.data.repositories

import com.openclassrooms.realestatemanager.data.SearchCriteria
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date

interface IRealtyRepository {
    var filteredRealties: List<Realty>
    val selectedRealtyFlow: StateFlow<Realty?>
    var updatedRealty: Realty?
    fun setSelectedRealty(realty: Realty?)
    suspend fun insertRealty(realty: Realty)
    fun getAllRealties(): Flow<List<Realty>>
    suspend fun getRealtyFromID(realtyID: Int): Realty?
    suspend fun updateRealty(realty: Realty)
    fun getFilteredRealtiesFlow(criteria: SearchCriteria?): Flow<List<Realty>>

}
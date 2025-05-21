package com.openclassrooms.realestatemanager.data.repositories

import android.content.Context
import com.openclassrooms.realestatemanager.data.SearchCriteria
import com.openclassrooms.realestatemanager.data.room.DatabaseProvider
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RealtyRepository(context: Context): IRealtyRepository {

    private val db = DatabaseProvider.getDatabase(context)
    private val dao = db.realtyDao()
    override var filteredRealties = emptyList<Realty>()
    override var allRealties: List<Realty> = emptyList()
    private val _selectedRealty = MutableStateFlow<Realty?>(null)
    override val selectedRealtyFlow: StateFlow<Realty?> = _selectedRealty
    override var updatedRealty: Realty? = null

    override fun setSelectedRealty(realty: Realty?) {
        _selectedRealty.value = realty
    }

    override suspend fun insertRealty(realty: Realty) {
        dao.insertRealty(realty)
    }

    override fun getAllRealties(): Flow<List<Realty>> {
        return dao.getAllRealties()
    }

    override fun getRealtyFromID(realtyID: Int) {
        _selectedRealty.value = allRealties.find { it.id == realtyID }
    }

    override suspend fun updateRealty(realty: Realty) {
        dao.updateRealty(realty)
    }

    override fun getFilteredRealtiesFlow(criteria: SearchCriteria?): Flow<List<Realty>> = flow {
        emit(
            dao.getFilteredRealties(
                criteria?.isAvailable,
                criteria?.minPrice?.toDouble(),
                criteria?.maxPrice?.toDouble(),
                criteria?.minSurface?.toDouble(),
                criteria?.maxSurface?.toDouble(),
                criteria?.minRooms,
                criteria?.maxRooms,
                criteria?.minEntryDate,
                criteria?.maxEntryDate,
                criteria?.minSoldDate,
                criteria?.maxSoldDate,
                criteria?.realtyTypes?.map { it.name },
                criteria?.realtyTypes?.size ?: 0,
            )
        )
    }.flowOn(Dispatchers.IO)



}
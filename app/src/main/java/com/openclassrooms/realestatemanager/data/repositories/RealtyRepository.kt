package com.openclassrooms.realestatemanager.data.repositories

import android.content.Context
import com.openclassrooms.realestatemanager.data.room.DatabaseProvider
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date

class RealtyRepository(context: Context): IRealtyRepository {

    private val db = DatabaseProvider.getDatabase(context)
    private val dao = db.realtyDao()

    override var allRealties: List<Realty> = emptyList()
    private val _selectedRealty = MutableStateFlow<Realty?>(null)
    override val selectedRealtyFlow: StateFlow<Realty?> = _selectedRealty
    private val _sortedRealities = MutableStateFlow<List<Realty>>(emptyList())
    override val sortedRealities: StateFlow<List<Realty>> = _sortedRealities
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

    override suspend fun updateRealty(realty: Realty) {
        dao.updateRealty(realty)
    }

    override suspend fun searchRealities(
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
    ) {
        if (isReset) {
            _sortedRealities.value = emptyList()
            return
        }
        dao.searchRealities(
            isAvailable,
            minPrice,
            maxPrice,
            minSurface,
            maxSurface,
            minRooms,
            maxRooms,
            minEntryDate,
            maxEntryDate,
            minSoldDate,
            maxSoldDate,
            realtyTypes,
            realtyTypes?.size ?: 0,
            amenity
        ).collect { result ->
            _sortedRealities.value = result
        }
    }
}
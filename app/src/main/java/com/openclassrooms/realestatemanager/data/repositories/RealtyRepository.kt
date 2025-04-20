package com.openclassrooms.realestatemanager.data.repositories

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.openclassrooms.realestatemanager.data.room.DatabaseProvider
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RealtyRepository(context: Context): IRealtyRepository {

    private val db = DatabaseProvider.getDatabase(context)
    private val dao = db.realtyDao()
    private val _selectedRealty = MutableStateFlow<Realty?>(null)
    override val selectedRealtyFlow: StateFlow<Realty?> = _selectedRealty
    override var sortedRealities: List<Realty> = emptyList()
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
}
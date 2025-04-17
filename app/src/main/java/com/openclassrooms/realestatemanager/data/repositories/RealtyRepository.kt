package com.openclassrooms.realestatemanager.data.repositories

import android.content.Context
import com.openclassrooms.realestatemanager.data.room.DatabaseProvider
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import kotlinx.coroutines.flow.Flow

class RealtyRepository(context: Context): IRealtyRepository {

    private val db = DatabaseProvider.getDatabase(context)
    private val dao = db.realtyDao()
    override var selectedRealty: Realty? = null
    override var sortedRealities: List<Realty> = emptyList()

    override suspend fun insertRealty(realty: Realty) {
        dao.insertRealty(realty)
    }

    override fun getAllRealties(): Flow<List<Realty>> {
       return dao.getAllRealties()
    }
}
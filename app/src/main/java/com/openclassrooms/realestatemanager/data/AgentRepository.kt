package com.openclassrooms.realestatemanager.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

class AgentRepository(context: Context): IAgentRepository {

    private val db = DatabaseProvider.getDatabase(context)
    private val dao = db.realtyAgentDao()

    override suspend fun insertAgent(name: String) {
        dao.insertAgent(RealtyAgent(name = name))
    }

    override fun getAllAgents(): Flow<List<RealtyAgent>> {
        return dao.getAllAgents()
    }

}
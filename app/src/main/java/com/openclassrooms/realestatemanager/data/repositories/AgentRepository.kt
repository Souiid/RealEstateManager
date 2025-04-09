package com.openclassrooms.realestatemanager.data.repositories

import android.content.Context
import com.openclassrooms.realestatemanager.data.room.DatabaseProvider
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
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

    override suspend fun getAgentByID(agentId: Int): RealtyAgent? {
        return dao.getAgentById(agentId)
    }


}
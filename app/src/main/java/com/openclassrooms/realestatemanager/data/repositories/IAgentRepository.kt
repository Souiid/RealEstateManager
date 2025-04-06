package com.openclassrooms.realestatemanager.data.repositories

import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import kotlinx.coroutines.flow.Flow

interface IAgentRepository {
    suspend fun insertAgent(name: String)
    fun getAllAgents(): Flow<List<RealtyAgent>>
}
package com.openclassrooms.realestatemanager.data

import kotlinx.coroutines.flow.Flow

interface IAgentRepository {
    suspend fun insertAgent(name: String)
    fun getAllAgents(): Flow<List<RealtyAgent>>
}
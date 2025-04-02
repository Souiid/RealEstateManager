package com.openclassrooms.realestatemanager.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RealtyAgentDao {
    @Insert
    suspend fun insertAgent(agent: RealtyAgent)

    @Query("SELECT * FROM agents")
    suspend fun getAllAgents(): List<RealtyAgent>

    @Query("SELECT * FROM agents WHERE id = :agentId LIMIT 1")
    suspend fun getAgentById(agentId: Int): RealtyAgent?
}
package com.openclassrooms.realestatemanager.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import kotlinx.coroutines.flow.Flow

@Dao
interface RealtyAgentDao {
    @Insert
    suspend fun insertAgent(agent: RealtyAgent)

    @Query("SELECT * FROM agents")
    fun getAllAgents(): Flow<List<RealtyAgent>>

    @Query("SELECT * FROM agents WHERE id = :agentId LIMIT 1")
    suspend fun getAgentById(agentId: Int): RealtyAgent?

}
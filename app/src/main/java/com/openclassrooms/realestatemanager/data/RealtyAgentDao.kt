package com.openclassrooms.realestatemanager.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface RealtyAgentDao {
    @Insert
    suspend fun insertAgent(agent: RealtyAgent)

    @Query("SELECT * FROM agents")
    fun getAllAgents(): Flow<List<RealtyAgent>>
}
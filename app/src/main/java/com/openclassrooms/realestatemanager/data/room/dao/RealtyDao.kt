package com.openclassrooms.realestatemanager.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import kotlinx.coroutines.flow.Flow

@Dao
interface RealtyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRealty(realty: Realty)

    @Query("SELECT * FROM realties")
    fun getAllRealties(): Flow<List<Realty>>

    @Transaction
    @Query("SELECT * FROM realties WHERE id = :id")
    suspend fun getRealtyById(id: String): Realty?


    @Update
    suspend fun updateRealty(realty: Realty)
}
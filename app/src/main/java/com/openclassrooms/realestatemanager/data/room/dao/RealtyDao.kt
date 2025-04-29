package com.openclassrooms.realestatemanager.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import kotlinx.coroutines.flow.Flow
import java.util.Date

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

    @Query("""
    SELECT * FROM realties
    WHERE (:isAvailable IS NULL OR isAvailable = :isAvailable)
    AND (:minPrice IS NULL OR price >= :minPrice)
    AND (:maxPrice IS NULL OR price <= :maxPrice)
    AND (:minSurface IS NULL OR surface >= :minSurface)
    AND (:maxSurface IS NULL OR surface <= :maxSurface)
    AND (:minRooms IS NULL OR roomsNbr >= :minRooms)
    AND (:entryDate IS NULL OR entryDate <= :entryDate)
    AND (:soldDate IS NULL OR (saleDate IS NOT NULL AND saleDate <= :soldDate))
    AND (:realtyTypes IS NULL OR realtyType IN (:realtyTypes))
    AND (:amenity IS NULL OR amenities LIKE '%' || :amenity || '%')
""")
    fun searchRealties(
        isAvailable: Boolean? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        minSurface: Double? = null,
        maxSurface: Double? = null,
        minRooms: Int? = null,
        entryDate: Date? = null,
        soldDate: Date? = null,
        realtyTypes: List<String>? = null,
        amenity: String? = null
    ): Flow<List<Realty>>
}
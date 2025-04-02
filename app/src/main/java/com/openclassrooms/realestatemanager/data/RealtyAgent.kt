package com.openclassrooms.realestatemanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agents")
data class RealtyAgent(
    @PrimaryKey val id: Int,
    val name: String
)

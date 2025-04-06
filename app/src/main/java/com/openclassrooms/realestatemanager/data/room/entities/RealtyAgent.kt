package com.openclassrooms.realestatemanager.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agents")
data class RealtyAgent(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)

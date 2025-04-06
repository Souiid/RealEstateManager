package com.openclassrooms.realestatemanager.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.data.RealtyPicture
import com.openclassrooms.realestatemanager.data.RealtyPrimaryInfo

@Entity(
    tableName = "realties",
    foreignKeys = [
        ForeignKey(
            entity = RealtyAgent::class,
            parentColumns = ["id"],
            childColumns = ["agentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("agentId")]
)
data class Realty(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val agentId: Int,

    @Embedded
    val primaryInfo: RealtyPrimaryInfo,

    @ColumnInfo(name = "pictures")
    val pictures: List<RealtyPicture>
)
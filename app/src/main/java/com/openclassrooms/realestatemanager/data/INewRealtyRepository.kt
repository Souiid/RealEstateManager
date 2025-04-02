package com.openclassrooms.realestatemanager.data

interface INewRealtyRepository {
    var realtyPrimaryInfo: RealtyPrimaryInfo?
    var images: List<RealtyPicture>?
    var db: AppDatabase
    var dao: RealtyAgentDao
    suspend fun insertAgent(id: Int, name: String)
    suspend fun getAllAgents(): List<RealtyAgent>
}
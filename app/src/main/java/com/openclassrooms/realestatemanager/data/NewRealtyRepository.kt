package com.openclassrooms.realestatemanager.data

import android.content.Context

class NewRealtyRepository(context: Context): INewRealtyRepository {
    override var realtyPrimaryInfo: RealtyPrimaryInfo? = null
    override var images: List<RealtyPicture>? = null

    override var db = DatabaseProvider.getDatabase(context)
    override var dao = db.realtyAgentDao()

    override suspend fun insertAgent(id: Int, name: String) {
        dao.insertAgent(RealtyAgent(id, name))
    }

    override suspend fun getAllAgents(): List<RealtyAgent> {
        return dao.getAllAgents()
    }
}
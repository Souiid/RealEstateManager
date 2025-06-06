package com.openclassrooms.realestatemanager.data.repositories

import android.content.Context
import com.openclassrooms.realestatemanager.data.RealtyPicture
import com.openclassrooms.realestatemanager.data.RealtyPrimaryInfo

class NewRealtyRepository(context: Context): INewRealtyRepository {
    override var realtyPrimaryInfo: RealtyPrimaryInfo? = null
    override var images: List<RealtyPicture>? = null

}
package com.openclassrooms.realestatemanager.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

class NewRealtyRepository(context: Context): INewRealtyRepository {
    override var realtyPrimaryInfo: RealtyPrimaryInfo? = null
    override var images: List<RealtyPicture>? = null

}
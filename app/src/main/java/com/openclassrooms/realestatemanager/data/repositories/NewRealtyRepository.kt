package com.openclassrooms.realestatemanager.data.repositories

import com.openclassrooms.realestatemanager.data.RealtyPicture
import com.openclassrooms.realestatemanager.data.RealtyPrimaryInfo

class NewRealtyRepository: INewRealtyRepository {
    override var realtyPrimaryInfo: RealtyPrimaryInfo? = null
    override var images: List<RealtyPicture>? = null

}
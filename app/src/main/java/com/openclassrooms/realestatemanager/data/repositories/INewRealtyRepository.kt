package com.openclassrooms.realestatemanager.data.repositories

import com.openclassrooms.realestatemanager.data.RealtyPicture
import com.openclassrooms.realestatemanager.data.RealtyPrimaryInfo

interface INewRealtyRepository {
    var realtyPrimaryInfo: RealtyPrimaryInfo?
    var images: List<RealtyPicture>?
}
package com.openclassrooms.realestatemanager.data

import kotlinx.coroutines.flow.Flow

interface INewRealtyRepository {
    var realtyPrimaryInfo: RealtyPrimaryInfo?
    var images: List<RealtyPicture>?
}
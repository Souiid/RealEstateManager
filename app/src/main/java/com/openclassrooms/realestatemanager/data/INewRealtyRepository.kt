package com.openclassrooms.realestatemanager.data

interface INewRealtyRepository {
    var realtyPrimaryInfo: RealtyPrimaryInfo?
    var images: List<RealtyPicture>?
}
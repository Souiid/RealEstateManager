package com.openclassrooms.realestatemanager.data

import android.graphics.Bitmap

data class RealtyPicture(
    @Transient val bitmap: Bitmap?,
    val description: String,
    val uriString: String)

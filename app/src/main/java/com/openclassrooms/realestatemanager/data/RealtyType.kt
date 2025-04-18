package com.openclassrooms.realestatemanager.data

import androidx.annotation.StringRes
import com.openclassrooms.realestatemanager.R

enum class RealtyType(@StringRes val labelResId: Int) {
    FLAT(R.string.flat),
    LOFT(R.string.loft),
    MANOR(R.string.manor),
    HOUSE(R.string.house),
    DUPLEX(R.string.duplex),
    PENTHOUSE(R.string.penthouse)
}
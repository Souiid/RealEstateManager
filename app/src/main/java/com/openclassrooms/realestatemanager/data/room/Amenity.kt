package com.openclassrooms.realestatemanager.data.room

import androidx.annotation.StringRes
import com.openclassrooms.realestatemanager.R

enum class Amenity(@StringRes val labelResId: Int) {
    SCHOOL(R.string.school),
    HOSPITAL(R.string.hospital),
    SUPERMARKET(R.string.supermarket),
    PARK(R.string.park),
    RESTAURANT(R.string.restaurant),
    PHARMACY(R.string.pharmacy),
    BUS_STOP(R.string.bus_stop),
    TRAIN_STATION(R.string.train_station),
    GYM(R.string.gym),
    BANK(R.string.bank),
    POST_OFFICE(R.string.post_office),
    BAKERY(R.string.bakery),
    METRO(R.string.metro)
}
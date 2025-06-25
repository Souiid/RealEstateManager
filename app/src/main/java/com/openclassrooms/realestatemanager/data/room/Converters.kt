package com.openclassrooms.realestatemanager.data.room

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.data.RealtyPicture
import com.openclassrooms.realestatemanager.data.RealtyPlace
import com.openclassrooms.realestatemanager.data.RealtyType
import java.util.Date

class Converters {
    @TypeConverter
    fun fromPictureList(value: List<RealtyPicture>): String = Gson().toJson(value)

    @TypeConverter
    fun toPictureList(value: String): List<RealtyPicture> {
        val type = object : TypeToken<List<RealtyPicture>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromRealtyType(type: RealtyType): String = type.name

    @TypeConverter
    fun toRealtyType(value: String): RealtyType = RealtyType.valueOf(value)

    @TypeConverter
    fun fromAmenityList(amenities: List<Amenity>): String {
        return Gson().toJson(amenities.map { it.name })
    }

    @TypeConverter
    fun toAmenityList(data: String): List<Amenity> {
        val type = object : TypeToken<List<String>>() {}.type
        val list = Gson().fromJson<List<String>>(data, type)
        return list.map { Amenity.valueOf(it) }
    }

    @TypeConverter
    fun fromRealtyPlace(value: RealtyPlace): String = Gson().toJson(value)

    @TypeConverter
    fun toRealtyPlace(value: String): RealtyPlace {
        val type = object : TypeToken<RealtyPlace>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
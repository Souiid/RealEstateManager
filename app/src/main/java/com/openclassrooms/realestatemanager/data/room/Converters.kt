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
    fun fromRealtyPlace(value: RealtyPlace): String = Gson().toJson(value)

    @TypeConverter
    fun toRealtyPlace(value: String): RealtyPlace {
        val type = object : TypeToken<RealtyPlace>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromLatLng(position: LatLng): String = Gson().toJson(position)

    @TypeConverter
    fun toLatLng(value: String): LatLng {
        val type = object : TypeToken<LatLng>() {}.type
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
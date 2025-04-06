package com.openclassrooms.realestatemanager.data.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.data.RealtyPicture
import com.openclassrooms.realestatemanager.data.RealtyPlace
import com.openclassrooms.realestatemanager.data.RealtyType

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
}
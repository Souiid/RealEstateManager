package com.openclassrooms.realestatemanager.data.room

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.openclassrooms.realestatemanager.data.RealtyPicture
import com.openclassrooms.realestatemanager.data.RealtyPlace
import com.openclassrooms.realestatemanager.data.RealtyPrimaryInfo
import com.openclassrooms.realestatemanager.data.RealtyType
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import kotlinx.coroutines.runBlocking
import androidx.core.net.toUri

class RealtyContentProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.openclassrooms.realestatemanager.provider"
        const val TABLE_NAME = "realties"
        val URI_REALTY: Uri = "content://$AUTHORITY/$TABLE_NAME".toUri()

        private const val CODE_REALTY_DIR = 1
        private const val CODE_REALTY_ITEM = 2

        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, TABLE_NAME, CODE_REALTY_DIR)
            addURI(AUTHORITY, "$TABLE_NAME/#", CODE_REALTY_ITEM)
        }
    }

    private lateinit var db: AppDatabase

    override fun onCreate(): Boolean {
        context?.let {
            db = DatabaseProvider.getDatabase(it)
        } ?: return false
        return true
    }

    override fun query(
        uri: Uri, projection: Array<out String>?, selection: String?,
        selectionArgs: Array<out String>?, sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            CODE_REALTY_DIR -> db.query("SELECT * FROM $TABLE_NAME", null)
            CODE_REALTY_ITEM -> {
                val id = ContentUris.parseId(uri)
                db.query("SELECT * FROM $TABLE_NAME WHERE id = $id", null)
            }

            else -> throw IllegalArgumentException("Unknown URI $uri")
        }.apply {
            setNotificationUri(context?.contentResolver, uri)
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (uriMatcher.match(uri) != CODE_REALTY_DIR || values == null) {
            throw IllegalArgumentException("Invalid URI or missing values: $uri")
        }

        val realty = Realty(
            id = 0,
            agentId = values.getAsInteger("agentId") ?: 0,
            entryDate = java.util.Date(values.getAsLong("entryDate") ?: System.currentTimeMillis()),
            saleDate = values.getAsLong("saleDate")?.let { java.util.Date(it) },
            isAvailable = values.getAsInteger("isAvailable") == 1,
            primaryInfo = RealtyPrimaryInfo(
                realtyType = RealtyType.valueOf(values.getAsString("realtyType") ?: RealtyType.FLAT.name),
                surface = values.getAsInteger("surface") ?: 0,
                price = values.getAsInteger("price") ?: 0,
                roomsNbr = values.getAsInteger("roomsNbr") ?: 0,
                bathroomsNbr = values.getAsInteger("bathroomsNbr") ?: 0,
                bedroomsNbr = values.getAsInteger("bedroomsNbr") ?: 0,
                description = values.getAsString("description") ?: "",
                realtyPlace = RealtyPlace(
                    id = values.getAsString("place_id") ?: "",
                    name = values.getAsString("place_name") ?: "",
                    positionLatLng = LatLng(
                        values.getAsDouble("lat") ?: 0.0,
                        values.getAsDouble("lng") ?: 0.0
                    )
                ),
                amenities = parseAmenities(values.getAsString("amenities") ?: ""),
                isEuro = values.getAsInteger("isEuro") == 1
            ),
            pictures = parsePictures(values.getAsString("pictures") ?: "[]")
        )

        val id = runBlocking {
            db.realtyDao().insertRealtyAndReturnId(realty)
        }

        context?.contentResolver?.notifyChange(uri, null)
        return ContentUris.withAppendedId(URI_REALTY, id)
    }

    private fun parsePictures(json: String): List<RealtyPicture> {
        return try {
            val type = object : TypeToken<List<RealtyPicture>>() {}.type
            Gson().fromJson(json, type)
        } catch (_: Exception) {
            emptyList()
        }
    }

    override fun getType(uri: Uri): String? = when (uriMatcher.match(uri)) {
        CODE_REALTY_DIR -> "vnd.android.cursor.dir/vnd.$AUTHORITY.$TABLE_NAME"
        CODE_REALTY_ITEM -> "vnd.android.cursor.item/vnd.$AUTHORITY.$TABLE_NAME"
        else -> null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0

    private fun parseAmenities(amenitiesString: String): List<Amenity> {
        return amenitiesString
            .split(",")
            .mapNotNull { name ->
                try {
                    Amenity.valueOf(name.trim().uppercase())
                } catch (_: Exception) {
                    null
                }
            }
    }
}
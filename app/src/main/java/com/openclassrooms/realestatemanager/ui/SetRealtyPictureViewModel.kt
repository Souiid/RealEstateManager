package com.openclassrooms.realestatemanager.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.data.INewRealtyRepository
import com.openclassrooms.realestatemanager.data.RealtyPicture

class SetRealtyPictureViewModel(val repository: INewRealtyRepository): ViewModel() {

    fun setRealtyPictures(realtyPictures: List<RealtyPicture>) {
        repository.images = realtyPictures
    }

    fun getRealtyPictures(): List<RealtyPicture>? {
        return repository.images
    }

    fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun createImageUri(context: Context): Uri? {
        val contentResolver = context.contentResolver
        val contentValues = android.content.ContentValues().apply {
            put(
                android.provider.MediaStore.MediaColumns.DISPLAY_NAME,
                "captured_image_${System.currentTimeMillis()}.jpg"
            )
            put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }

        val uri = contentResolver.insert(
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        if (uri == null) {
            Log.e("createImageUri", "Failed to create image URI")
        } else {
            Log.d("createImageUri", "Generated Uri: $uri")
        }

        return uri
    }
}
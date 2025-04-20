package com.openclassrooms.realestatemanager.ui.screens.form.setrealtypicture

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.RealtyPicture
import com.openclassrooms.realestatemanager.data.Utils
import com.openclassrooms.realestatemanager.data.repositories.INewRealtyRepository
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import kotlinx.coroutines.launch

class SetRealtyPictureViewModel(
    private val newRealtyRepository: INewRealtyRepository,
    private val realtyRepository: IRealtyRepository
): ViewModel() {

    fun setRealtyPictures(realtyPictures: List<RealtyPicture>, updatedRealty: Realty? = null, completion: ()->Unit) {
        if (updatedRealty == null) {
            newRealtyRepository.images = realtyPictures
            completion()
        }else {
            viewModelScope.launch {
                val freshRealty = updatedRealty.copy(pictures = realtyPictures)
                realtyRepository.setSelectedRealty(freshRealty)
                realtyRepository.updateRealty(realty = freshRealty)
                realtyRepository.updatedRealty = null
                completion()
            }
        }
    }

    fun getUpdatedRealty(): Realty? {
        return realtyRepository.updatedRealty
    }

    fun getRealtyPictures(): List<RealtyPicture>? {
        return newRealtyRepository.images
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

    fun uriToBitmapLegacy(context: Context, uri: Uri): Bitmap? {
        return Utils().uriToBitmapLegacy(context, uri)
    }
}
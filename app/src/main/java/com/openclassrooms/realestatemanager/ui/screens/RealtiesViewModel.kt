package com.openclassrooms.realestatemanager.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import kotlinx.coroutines.flow.Flow

class RealtiesViewModel(
    private val realtyRepository: IRealtyRepository
): ViewModel() {

    val realties: Flow<List<Realty>> = realtyRepository.getAllRealties()

    fun setSelectedRealty(realty: Realty) {
        realtyRepository.selectedRealty = realty
    }

    fun uriToBitmapLegacy(context: Context, uri: Uri): Bitmap? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
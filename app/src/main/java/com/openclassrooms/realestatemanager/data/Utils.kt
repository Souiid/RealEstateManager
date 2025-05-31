package com.openclassrooms.realestatemanager.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import androidx.compose.ui.unit.dp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class Utils {

    fun convertDollarToEuro(dollars: Int): Int {
        return Math.round(dollars * 0.812).toInt()
    }

    fun convertEuroToDollar(euros: Int): Int {
        return Math.round(euros * 1.15).toInt()
    }

    fun convertEuroToDollarDouble(euros: Double): Double {
        return Math.round(euros * 1.15).toDouble()
    }

    fun getCorrectPriceComponent(price: Int = 0, isEuro: Boolean = true): PriceComponent {
        return if (isEuro) {
            PriceComponent(price, "€")
        }else {
            PriceComponent(convertEuroToDollar(price), "$")
        }
    }

    fun getTodayDate(date: Date): String {
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        return dateFormat.format(date)
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    companion object {
        val TITLE_SIZE = 30.dp
        val DESCRIPTION_SIZE = 20.dp
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
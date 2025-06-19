package com.openclassrooms.realestatemanager.data

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.Uri
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class Utils {

    private val euroToDollarRate = 1.15
    private val dollarToEuroRate = 1 / euroToDollarRate
    private val _internetStatus = MutableStateFlow(true)
    val internetStatus: StateFlow<Boolean> = _internetStatus

    //Méthode imprecise pour convertir les devises, lorsque l'utilisateur choisi euro ou dollar
  // fun convertDollarToEuro(dollars: Int): Int {
  //     return Math.round(dollars * 0.812).toInt()
  // }
  //
  // fun convertEuroToDollar(euros: Int): Int {
  //     return Math.round(euros * 1.15).toInt()
  // }

    infix fun convertEuroToDollar(euros: Int): Int {
        return Math.round(euros * euroToDollarRate).toInt()
    }

    fun convertDollarToEuro(dollars: Int): Int {
        return Math.round(dollars * dollarToEuroRate).toInt()
    }

    fun convertEuroToDollarDouble(euros: Double): Double {
        return Math.round(euros * 1.15).toDouble()
    }

    fun getCorrectPriceComponent(price: Int = 0, isEuro: Boolean = true, isSaveInDollar: Boolean = false): PriceComponent {
        val finalPrice = when {
            isEuro && isSaveInDollar -> convertDollarToEuro(price)
            !isEuro && !isSaveInDollar -> convertEuroToDollar(price)
            else -> price
        }

        val currencySymbol = if (isEuro) "€" else "$"
        return PriceComponent(finalPrice, currencySymbol)
    }

    @SuppressLint("SimpleDateFormat")
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

    fun filterOnlyDigits(input: String): String {
        return input.filter { it.isDigit() }
    }

    fun filterNumericInput(input: String, allowDecimal: Boolean = true): String {
        val allowed = if (allowDecimal) "0123456789.," else "0123456789"
        var filtered = input.filter { it in allowed }

        filtered = filtered.replace(',', '.')

        if (allowDecimal && filtered.count { it == '.' } > 1) {
            val first = filtered.indexOf('.')
            filtered =
                filtered.substring(0, first + 1) + filtered.substring(first + 1).replace(".", "")
        }

        return filtered
    }


    fun isTablet(context: Context): Boolean {
        val configuration = context.resources.configuration
        return configuration.smallestScreenWidthDp >= 600
    }

    fun startInternetMonitoring(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _internetStatus.value = true
            }

            override fun onLost(network: Network) {
                _internetStatus.value = false
            }
        })
    }
}
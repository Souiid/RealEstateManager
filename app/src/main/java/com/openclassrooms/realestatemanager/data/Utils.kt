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
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

class Utils {

    private val euroToDollarRate = 1.15
    private val dollarToEuroRate = 1 / euroToDollarRate
    private val _internetStatus = MutableStateFlow(false)
    val internetStatus: StateFlow<Boolean> = _internetStatus

    //Imprecise method for converting currencies when the user selects euro or dollar
  // fun convertDollarToEuro(dollars: Int): Int {
  //     return Math.round(dollars * 0.812).toInt()
  // }
  //
  // fun convertEuroToDollar(euros: Int): Int {
  //     return Math.round(euros * 1.15).toInt()
  // }

    infix fun convertEuroToDollar(euros: Int): Int {
        return (euros * euroToDollarRate).roundToInt()
    }

    fun convertDollarToEuro(dollars: Int): Int {
        return (dollars * dollarToEuroRate).roundToInt()
    }

    fun convertEuroToDollarDouble(euros: Double): Double {
        return (euros * 1.15).roundToInt().toDouble()
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

        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        val isConnected = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        _internetStatus.value = isConnected

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

    fun calculateDistanceInKm(start: LatLng, end: LatLng): Double {
        val earthRadiusKm = 6371.0

        val dLat = Math.toRadians(end.latitude - start.latitude)
        val dLon = Math.toRadians(end.longitude - start.longitude)

        val lat1 = Math.toRadians(start.latitude)
        val lat2 = Math.toRadians(end.latitude)

        val a = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(lat1) * cos(lat2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadiusKm * c
    }

}
package com.openclassrooms.realestatemanager.data

import android.content.Context
import android.net.wifi.WifiManager
import androidx.compose.ui.unit.dp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class Utils {

    fun convertDollarToEuro(dollars: Int): Int {
        return Math.round(dollars * 0.812).toInt()
    }

    fun getTodayDate(): String {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd")
        return dateFormat.format(Date())
    }

    fun isInternetAvailable(context: Context): Boolean {
        val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifi.isWifiEnabled
    }

    companion object {
        val TITLE_SIZE = 30.dp
        val DESCRIPTION_SIZE = 20.dp
    }
}
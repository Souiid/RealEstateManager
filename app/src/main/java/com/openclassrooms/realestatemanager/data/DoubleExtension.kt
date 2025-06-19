package com.openclassrooms.realestatemanager.data

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun Double.formatSmart(): String {
    return if (this % 1.0 == 0.0) {
        String.format("%.0f", this)
    } else {
        String.format("%.2f", this).replace('.', ',')
    }
}
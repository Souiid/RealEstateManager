package com.openclassrooms.realestatemanager.data

fun Double.formatSmart(): String {
    return if (this % 1.0 == 0.0) {
        this.toInt().toString()
    } else {
        String.format("%.2f", this)
    }
}
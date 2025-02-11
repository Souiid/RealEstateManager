package com.openclassrooms.realestatemanager

import android.app.Application
import com.openclassrooms.realestatemanager.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RealEstateManager: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RealEstateManager)
            modules(
                appModule
            )
        }
    }
}
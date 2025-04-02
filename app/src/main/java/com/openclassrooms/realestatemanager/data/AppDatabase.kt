package com.openclassrooms.realestatemanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RealtyAgent::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun realtyAgentDao(): RealtyAgentDao
}

object DatabaseProvider {

    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "my_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
package com.openclassrooms.realestatemanager.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

object DataStoreManager {

    private val IS_EURO = booleanPreferencesKey("is_euro")

    fun getIsEuro(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_EURO] ?: true
        }
    }

    suspend fun setIsEuro(context: Context, enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_EURO] = enabled
        }
    }

}
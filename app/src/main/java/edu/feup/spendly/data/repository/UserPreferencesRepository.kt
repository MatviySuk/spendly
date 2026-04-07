package edu.feup.spendly.data.repository

import android.content.Context
import android.provider.Settings
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID

/**
 * DataStore for user preferences.
 * Requirement 3.3: Data should not exist exclusively in memory.
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferencesRepository(private val context: Context) {

    private val darkThemeKey = booleanPreferencesKey("dark_theme")
    private val budgetKey = doublePreferencesKey("budget")
    private val deviceIdKey = stringPreferencesKey("device_id")

    val darkThemeFlow: Flow<Boolean?> = context.dataStore.data
        .map { preferences ->
            preferences[darkThemeKey]
        }

    val budgetFlow: Flow<Double> = context.dataStore.data
        .map { preferences ->
            preferences[budgetKey] ?: 0.0
        }

    /**
     * Requirement 3.7: Returns a unique ID for this device/user.
     * This allows multiple devices to sync to the same Firebase project under different nodes.
     */
    suspend fun getDeviceId(): String {
        val preferences = context.dataStore.data.first()
        val existingId = preferences[deviceIdKey]
        
        if (existingId != null) return existingId
        
        // Generate a new ID if none exists (e.g., ANDROID_ID or random UUID)
        val newId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) 
            ?: UUID.randomUUID().toString()
            
        context.dataStore.edit { prefs ->
            prefs[deviceIdKey] = newId
        }
        return newId
    }

    suspend fun updateDarkTheme(isDark: Boolean?) {
        context.dataStore.edit { preferences ->
            if (isDark == null) {
                preferences.remove(darkThemeKey)
            } else {
                preferences[darkThemeKey] = isDark
            }
        }
    }

    suspend fun updateBudget(budget: Double) {
        context.dataStore.edit { preferences ->
            preferences[budgetKey] = budget
        }
    }
}

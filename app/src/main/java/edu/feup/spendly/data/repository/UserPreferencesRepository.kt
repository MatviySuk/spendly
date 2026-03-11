package edu.feup.spendly.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DataStore for user preferences.
 * Requirement 3.3: Data should not exist exclusively in memory.
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferencesRepository(private val context: Context) {

    private val currencyKey = stringPreferencesKey("currency")

    val currencyFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[currencyKey] ?: "EUR"
        }

    suspend fun updateCurrency(currency: String) {
        context.dataStore.edit { preferences ->
            preferences[currencyKey] = currency
        }
    }
}

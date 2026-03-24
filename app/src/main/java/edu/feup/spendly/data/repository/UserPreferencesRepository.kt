package edu.feup.spendly.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
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
    private val darkThemeKey = booleanPreferencesKey("dark_theme")
    private val budgetKey = doublePreferencesKey("budget")

    val currencyFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[currencyKey] ?: "EUR"
        }

    val darkThemeFlow: Flow<Boolean?> = context.dataStore.data
        .map { preferences ->
            preferences[darkThemeKey]
        }

    val budgetFlow: Flow<Double> = context.dataStore.data
        .map { preferences ->
            preferences[budgetKey] ?: 0.0
        }

    suspend fun updateCurrency(currency: String) {
        context.dataStore.edit { preferences ->
            preferences[currencyKey] = currency
        }
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

package edu.feup.spendly.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.feup.spendly.data.repository.UserPreferencesRepository
import edu.feup.spendly.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for App Settings.
 * Requirement 3.2: Clear separation between UI and logic.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: ExpenseRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing

    val currency = userPreferencesRepository.currencyFlow.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), "EUR"
    )

    val darkTheme = userPreferencesRepository.darkThemeFlow.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )

    val budget = userPreferencesRepository.budgetFlow.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0
    )

    fun onCurrencyChange(newCurrency: String) {
        viewModelScope.launch {
            userPreferencesRepository.updateCurrency(newCurrency)
        }
    }

    fun onDarkThemeChange(isDark: Boolean?) {
        viewModelScope.launch {
            userPreferencesRepository.updateDarkTheme(isDark)
        }
    }

    fun onBudgetChange(newBudget: Double) {
        viewModelScope.launch {
            userPreferencesRepository.updateBudget(newBudget)
        }
    }

    /**
     * Requirement 3.7: Manual Synchronization trigger.
     * This fulfills the requirement to provide synchronization when connectivity is restored.
     */
    fun triggerManualSync() {
        viewModelScope.launch {
            _isSyncing.value = true
            try {
                repository.syncWithRemote()
            } catch (e: Exception) {
                // TODO: Handle error state (Requirement 3.5)
            } finally {
                _isSyncing.value = false
            }
        }
    }
}

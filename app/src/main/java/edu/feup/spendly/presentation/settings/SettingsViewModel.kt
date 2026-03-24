package edu.feup.spendly.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.feup.spendly.data.connectivity.ConnectivityObserver
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
    private val userPreferencesRepository: UserPreferencesRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing

    val connectivityStatus = connectivityObserver.observe()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ConnectivityObserver.Status.Unavailable)

    val darkTheme = userPreferencesRepository.darkThemeFlow.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )

    val budget = userPreferencesRepository.budgetFlow.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0
    )

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

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
                _error.value = "Cloud synchronization failed. Please check your internet connection."
            } finally {
                _isSyncing.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}

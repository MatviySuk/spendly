package edu.feup.spendly.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.feup.spendly.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for App Settings.
 * Requirement 3.2: Clear separation between UI and logic.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing

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

    /**
     * TODO: Implement user preferences management.
     * Requirement 3.3: Persistent storage for settings (e.g., Currency, Theme).
     * Hint: Use Jetpack DataStore for light-weight persistence.
     */
}

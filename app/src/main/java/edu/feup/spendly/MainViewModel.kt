package edu.feup.spendly

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.feup.spendly.data.connectivity.ConnectivityObserver
import edu.feup.spendly.data.repository.UserPreferencesRepository
import edu.feup.spendly.domain.repository.ExpenseRepository
import edu.feup.spendly.domain.usecase.AddExpenseUseCase
import edu.feup.spendly.domain.usecase.ProcessNfcTagUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository,
    private val processNfcTagUseCase: ProcessNfcTagUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val expenseRepository: ExpenseRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    val darkTheme: StateFlow<Boolean?> = userPreferencesRepository.darkThemeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val connectivityStatus = connectivityObserver.observe()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ConnectivityObserver.Status.Unavailable)

    init {
        // Requirement 3.7: Synchronize data exactly when connectivity is restored
        viewModelScope.launch {
            connectivityStatus.collectLatest { status ->
                if (status == ConnectivityObserver.Status.Available) {
                    expenseRepository.syncWithRemote()
                }
            }
        }
    }

    fun handleNfcIntent(intent: Intent) {
        val expense = processNfcTagUseCase(intent)
        if (expense != null) {
            viewModelScope.launch {
                addExpenseUseCase(expense)
            }
        }
    }
}

package edu.feup.spendly.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.feup.spendly.data.repository.UserPreferencesRepository
import edu.feup.spendly.domain.model.Expense
import edu.feup.spendly.domain.repository.ExpenseRepository
import edu.feup.spendly.domain.usecase.GetExpensesUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI State for the Home Screen.
 */
data class HomeUiState(
    val expenses: List<Expense> = emptyList(),
    val totalBalance: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasMore: Boolean = false
)

/**
 * ViewModel for the Home Dashboard.
 * Requirement 3.2: Clear separation between UI and logic.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val repository: ExpenseRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _displayLimit = MutableStateFlow(5)

    val currency: StateFlow<String> = userPreferencesRepository.currencyFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "EUR")

    val budget: StateFlow<Double> = userPreferencesRepository.budgetFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val darkTheme: StateFlow<Boolean?> = userPreferencesRepository.darkThemeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    
    /**
     * Requirement 3.3: Observe expenses from Repository via UseCase.
     * Requirement 3.5: Asynchronous processing using StateFlow.
     * Combines multiple flows into a single UiState.
     */
    val uiState: StateFlow<HomeUiState> = combine(
        getExpensesUseCase(),
        _isLoading,
        _displayLimit
    ) { expenses, loading, limit ->
        HomeUiState(
            expenses = expenses.take(limit),
            totalBalance = expenses.sumOf { it.amount },
            isLoading = loading,
            hasMore = expenses.size > limit
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )

    fun loadMore() {
        _displayLimit.value += 5
    }

    fun toggleTheme(currentDark: Boolean?) {
        viewModelScope.launch {
            userPreferencesRepository.updateDarkTheme(!(currentDark ?: false))
        }
    }

    /**
     * Requirement 3.7: Manual synchronization trigger.
     */
    fun syncData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.syncWithRemote()
            } catch (e: Exception) {
                // TODO: Handle error state in HomeUiState (Requirement 3.5)
            } finally {
                _isLoading.value = false
            }
        }
    }
}

package edu.feup.spendly.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val error: String? = null
)

/**
 * ViewModel for the Home Dashboard.
 * Requirement 3.2: Clear separation between UI and logic.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val repository: ExpenseRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    
    /**
     * Requirement 3.3: Observe expenses from Repository via UseCase.
     * Requirement 3.5: Asynchronous processing using StateFlow.
     * Combines multiple flows into a single UiState.
     */
    val uiState: StateFlow<HomeUiState> = combine(
        getExpensesUseCase(),
        _isLoading
    ) { expenses, loading ->
        HomeUiState(
            expenses = expenses,
            totalBalance = expenses.sumOf { it.amount },
            isLoading = loading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )

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

    /**
     * TODO: Implement more complex logic to calculate total balance for the current month.
     * Hint: Use the 'date' field in the Expense model to filter expenses.
     */
}

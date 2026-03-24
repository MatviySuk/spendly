package edu.feup.spendly.presentation.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.feup.spendly.domain.model.Expense
import edu.feup.spendly.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for Expense Analysis.
 * Requirement 3.8: Application/domain logic separation.
 */
@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {

    /**
     * Requirement 3.5: Asynchronous processing using StateFlow.
     * Expenses grouped by category with calculated totals.
     */
    val categoryTotals: StateFlow<Map<String, Double>> = repository.getExpenses()
        .map { expenses ->
            expenses.groupBy { it.category }
                .mapValues { entry -> entry.value.sumOf { it.amount } }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )
}

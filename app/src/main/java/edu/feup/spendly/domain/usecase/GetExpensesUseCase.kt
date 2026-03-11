package edu.feup.spendly.domain.usecase

import edu.feup.spendly.domain.model.Expense
import edu.feup.spendly.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to retrieve all expenses.
 * Requirement 3.8: Clean Architecture - Domain Layer.
 * Benefits: Business logic encapsulation, reusability across ViewModels.
 */
class GetExpensesUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    /**
     * Executes the use case.
     * Use 'invoke' operator for a clean syntax: getExpensesUseCase()
     */
    operator fun invoke(): Flow<List<Expense>> {
        return repository.getExpenses()
    }
}

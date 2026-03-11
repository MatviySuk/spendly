package edu.feup.spendly.domain.usecase

import edu.feup.spendly.domain.model.Expense
import edu.feup.spendly.domain.repository.ExpenseRepository
import javax.inject.Inject

/**
 * Use case to add a new expense.
 * Requirement 3.8: Clean Architecture - Domain Layer.
 */
class AddExpenseUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense) {
        repository.addExpense(expense)
    }
}

package edu.feup.spendly.domain.repository

import edu.feup.spendly.domain.model.Expense
import kotlinx.coroutines.flow.Flow

/**
 * Interface for Expense Repository.
 * This defines the contract for data operations.
 */
interface ExpenseRepository {

    /**
     * Requirement 3.3: Observe all expenses from local storage.
     */
    fun getExpenses(): Flow<List<Expense>>

    /**
     * Requirement 3.3 & 3.4: Add an expense.
     * Should save locally first (Offline-First) and then attempt remote sync.
     */
    suspend fun addExpense(expense: Expense)

    /**
     * Requirement 3.7: Synchronize local unsynced data with the remote server.
     */
    suspend fun syncWithRemote()
}

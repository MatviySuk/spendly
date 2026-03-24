package edu.feup.spendly.data.repository

import edu.feup.spendly.data.local.dao.ExpenseDao
import edu.feup.spendly.data.mapper.toDomain
import edu.feup.spendly.data.mapper.toDto
import edu.feup.spendly.data.mapper.toEntity
import edu.feup.spendly.data.remote.api.ExpenseApi
import edu.feup.spendly.domain.model.Expense
import edu.feup.spendly.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of ExpenseRepository.
 * Requirement 3.7 & 3.8: Orchestrates data between local and remote sources.
 */
class ExpenseRepositoryImpl(
    private val expenseDao: ExpenseDao,
    private val expenseApi: ExpenseApi
) : ExpenseRepository {

    override fun getExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addExpense(expense: Expense) {
        // Requirement 3.7: Offline-First - Save locally first
        expenseDao.insertExpense(expense.toEntity())

        // Requirement 3.4: Immediate sync attempt (Requirement 3.5: Async)
        try {
            val response = expenseApi.uploadExpense(expense.id, expense.toDto())
            if (response.isSuccessful) {
                expenseDao.markAsSynced(expense.id)
            }
        } catch (e: Exception) {
            // Log error or let WorkManager handle retry (Requirement 3.5 & 3.7)
        }
    }

    override suspend fun syncWithRemote() {
        /**
         * Requirement 3.7: Robust synchronization logic.
         */
        // 1. Upload unsynced local data
        val unsynced = expenseDao.getUnsyncedExpenses()
        unsynced.forEach { entity ->
            try {
                val response = expenseApi.uploadExpense(entity.id, entity.toDomain().toDto())
                if (response.isSuccessful) {
                    expenseDao.markAsSynced(entity.id)
                }
            } catch (e: Exception) {
                // Network failure, skip for now
            }
        }

        // 2. Download remote data and merge
        try {
            val remoteExpenses = expenseApi.getExpenses()
            remoteExpenses?.values?.forEach { dto ->
                expenseDao.insertExpense(dto.toEntity(isSynced = true))
            }
        } catch (e: Exception) {
            // Network failure
        }
    }
}

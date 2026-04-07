package edu.feup.spendly.data.repository

import android.util.Log
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
    private val expenseApi: ExpenseApi,
    private val userPreferencesRepository: UserPreferencesRepository
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
            val userId = userPreferencesRepository.getDeviceId()
            Log.d("SyncDebug", "Attempting to upload expense: ${expense.id} for user: $userId")
            val response = expenseApi.uploadExpense(userId, expense.id, expense.toDto())
            if (response.isSuccessful) {
                Log.d("SyncDebug", "Sync Successful for ${expense.id}")
                expenseDao.markAsSynced(expense.id)
            } else {
                Log.e("SyncDebug", "Sync Failed: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("SyncDebug", "Network Error during sync", e)
        }
    }

    override suspend fun syncWithRemote() {
        /**
         * Requirement 3.7: Robust synchronization logic.
         */
        val userId = userPreferencesRepository.getDeviceId()
        Log.d("SyncDebug", "Starting manual/background sync for user: $userId")
        
        // 1. Upload unsynced local data
        val unsynced = expenseDao.getUnsyncedExpenses()
        unsynced.forEach { entity ->
            try {
                val response = expenseApi.uploadExpense(userId, entity.id, entity.toDomain().toDto())
                if (response.isSuccessful) {
                    expenseDao.markAsSynced(entity.id)
                }
            } catch (e: Exception) {
                Log.e("SyncDebug", "Error syncing ${entity.id}", e)
            }
        }

        // 2. Download remote data and merge
        try {
            Log.d("SyncDebug", "Fetching remote data for user: $userId")
            val remoteExpenses = expenseApi.getExpenses(userId)
            Log.d("SyncDebug", "Remote data size: ${remoteExpenses?.size ?: 0}")
            remoteExpenses?.values?.forEach { dto ->
                expenseDao.insertExpense(dto.toEntity(isSynced = true))
            }
        } catch (e: Exception) {
            Log.e("SyncDebug", "Error fetching remote data", e)
        }
    }
}

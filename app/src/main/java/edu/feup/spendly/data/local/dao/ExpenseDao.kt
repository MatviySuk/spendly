package edu.feup.spendly.data.local.dao

import androidx.room.*
import edu.feup.spendly.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the expenses table.
 * Requirement 3.3: Definition of a data model and local storage.
 */
@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    /**
     * TODO: Implement a query to get only unsynced expenses.
     * Requirement 3.7: This is crucial for the offline-first synchronization strategy.
     */
    @Query("SELECT * FROM expenses WHERE isSynced = 0")
    suspend fun getUnsyncedExpenses(): List<ExpenseEntity>

    /**
     * TODO: Implement a query to mark an expense as synced after successful network upload.
     */
    @Query("UPDATE expenses SET isSynced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: String)
}

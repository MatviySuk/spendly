package edu.feup.spendly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.feup.spendly.data.local.dao.ExpenseDao
import edu.feup.spendly.data.local.entity.ExpenseEntity

/**
 * Room database for Spendly.
 * Requirement 3.3: Persistent local storage.
 */
@Database(entities = [ExpenseEntity::class], version = 1, exportSchema = false)
abstract class SpendlyDatabase : RoomDatabase() {
    abstract val expenseDao: ExpenseDao

    companion object {
        const val DATABASE_NAME = "spendly_db"
    }
}

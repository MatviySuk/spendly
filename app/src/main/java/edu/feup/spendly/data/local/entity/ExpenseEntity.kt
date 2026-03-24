package edu.feup.spendly.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity representing an Expense in the local database.
 * Requirement 3.3: Use of a persistent local storage mechanism.
 */
@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey val id: String,
    val amount: Double,
    val category: String,
    val date: Long,
    val location: String?,
    val notes: String?,
    val isSynced: Boolean = false
)

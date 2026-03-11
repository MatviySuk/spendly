package edu.feup.spendly.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import edu.feup.spendly.domain.model.Expense

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

/**
 * TODO: Implement mappers to/from Domain Model (Expense.kt)
 * Hint: Create extension functions to convert ExpenseEntity -> Expense and vice versa.
 */
fun ExpenseEntity.toDomain(): Expense = Expense(
    id = id,
    amount = amount,
    category = category,
    date = date,
    location = location,
    notes = notes,
    isSynced = isSynced
)

fun Expense.toEntity(): ExpenseEntity = ExpenseEntity(
    id = id,
    amount = amount,
    category = category,
    date = date,
    location = location,
    notes = notes,
    isSynced = isSynced
)

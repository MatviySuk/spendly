package edu.feup.spendly.data.mapper

import edu.feup.spendly.data.local.entity.ExpenseEntity
import edu.feup.spendly.data.remote.api.ExpenseDto
import edu.feup.spendly.domain.model.Expense

/**
 * Mappers to convert between different data layers.
 * Requirement 3.8: Clear separation between UI, Domain, and Data layers.
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

fun Expense.toDto(): ExpenseDto = ExpenseDto(
    id = id,
    amount = amount,
    category = category,
    date = date,
    location = location,
    notes = notes
)

fun ExpenseDto.toEntity(isSynced: Boolean): ExpenseEntity = ExpenseEntity(
    id = id,
    amount = amount,
    category = category,
    date = date,
    location = location,
    notes = notes,
    isSynced = isSynced
)

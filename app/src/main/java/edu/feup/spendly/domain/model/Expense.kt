package edu.feup.spendly.domain.model

import java.util.UUID

/**
 * Domain model representing an Expense.
 * This is the core business entity used throughout the app.
 */
data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val amount: Double,
    val category: String,
    val date: Long, // Timestamp
    val location: String? = null,
    val notes: String? = null,
    val isSynced: Boolean = false
)

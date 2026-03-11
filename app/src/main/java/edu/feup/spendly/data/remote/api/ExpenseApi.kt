package edu.feup.spendly.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Retrofit API interface for remote synchronization.
 * Requirement 3.4: REST/HTTP service integration.
 */
interface ExpenseApi {

    /**
     * Requirement 3.4: At least one GET request.
     * Fetches all expenses from the remote server.
     */
    @GET("expenses")
    suspend fun getExpenses(): List<ExpenseDto>

    /**
     * Requirement 3.4: At least one POST or PUT request.
     * Uploads a new expense to the remote server.
     */
    @POST("expenses")
    suspend fun uploadExpense(@Body expense: ExpenseDto): Response<Unit>
}

/**
 * Data Transfer Object for remote communication.
 * TODO: Implement a mapper to convert ExpenseDto -> Domain Expense.
 */
data class ExpenseDto(
    val id: String,
    val amount: Double,
    val category: String,
    val date: Long,
    val location: String?,
    val notes: String?
)

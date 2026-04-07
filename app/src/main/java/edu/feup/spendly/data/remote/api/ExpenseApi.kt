package edu.feup.spendly.data.remote.api

import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Retrofit API interface for remote synchronization.
 * Requirement 3.4: REST/HTTP service integration.
 */
interface ExpenseApi {

    /**
     * Requirement 3.4: At least one GET request.
     * Fetches all expenses from the remote server for a specific user/device.
     */
    @GET("users/{userId}/expenses.json")
    suspend fun getExpenses(
        @Path("userId") userId: String
    ): Map<String, ExpenseDto>?

    /**
     * Requirement 3.4: At least one POST or PUT request.
     * Uploads a new expense to the remote server under a specific user/device node.
     */
    @PUT("users/{userId}/expenses/{id}.json")
    suspend fun uploadExpense(
        @Path("userId") userId: String,
        @Path("id") id: String,
        @Body expense: ExpenseDto
    ): Response<ExpenseDto>
}

/**
 * Data Transfer Object for remote communication.
 */
@Serializable
data class ExpenseDto(
    val id: String,
    val amount: Double,
    val category: String,
    val date: Long,
    val location: String? = null,
    val notes: String? = null
)

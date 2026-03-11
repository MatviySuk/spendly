package edu.feup.spendly.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import edu.feup.spendly.domain.repository.ExpenseRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

/**
 * Background Worker for synchronizing local data to the cloud.
 * Requirement 3.7: Synchronize data when connectivity is restored.
 * Requirement 3.5: Proper handling of background operations.
 */
class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    /**
     * EntryPoint to access the Repository from WorkManager (which is not managed by Hilt).
     * TODO: Use @HiltWorker once 'androidx.hilt:hilt-work' is added to dependencies.
     */
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SyncWorkerEntryPoint {
        fun expenseRepository(): ExpenseRepository
    }

    override suspend fun doWork(): Result {
        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext, 
            SyncWorkerEntryPoint::class.java
        )
        val repository = entryPoint.expenseRepository()

        return try {
            repository.syncWithRemote()
            Result.success()
        } catch (e: Exception) {
            // Requirement 3.5: Handle errors gracefully and retry.
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}

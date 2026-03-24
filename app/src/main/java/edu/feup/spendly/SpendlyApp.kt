package edu.feup.spendly

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import edu.feup.spendly.data.worker.SyncWorker
import java.util.concurrent.TimeUnit

/**
 * Application class for Spendly.
 * Annotated with @HiltAndroidApp to trigger Hilt's code generation.
 */
@HiltAndroidApp
class SpendlyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setupBackgroundSync()
    }

    /**
     * Requirement 3.7: Background Processing / WorkManager.
     * Schedules a periodic background task to sync local data with the remote server.
     */
    private fun setupBackgroundSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Only run when online
            .build()

        // Schedule to run periodically (e.g., every 15 minutes, which is the minimum allowed by Android)
        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "SyncExpensesWork",
            ExistingPeriodicWorkPolicy.KEEP, // Keep the existing schedule if it's already running
            syncWorkRequest
        )
    }
}

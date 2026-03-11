package edu.feup.spendly.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Screen for App Settings.
 * Requirement 3.2: Jetpack Compose UI.
 * Requirement 3.7: Offline-First strategy / Manual Sync trigger.
 */
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {
    val isSyncing by viewModel.isSyncing.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        /**
         * Requirement 3.7: Trigger manual cloud synchronization.
         */
        Button(
            onClick = { viewModel.triggerManualSync() },
            enabled = !isSyncing,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isSyncing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Syncing...")
            } else {
                Text("Synchronize with Cloud")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        /**
         * TODO: Implement user preferences (Requirement 3.3).
         * 1. Toggle for Dark Mode.
         * 2. Currency selection (EUR, USD, etc.).
         * 3. Clear data option.
         */
        Text(text = "Theme and Currency settings should be implemented here.")
    }
}

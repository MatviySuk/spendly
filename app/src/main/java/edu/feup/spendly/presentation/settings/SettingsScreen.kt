package edu.feup.spendly.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.feup.spendly.data.connectivity.ConnectivityObserver
import java.util.Locale

/**
 * Screen for App Settings.
 * Requirement 3.2: Jetpack Compose UI.
 * Requirement 3.3: Persistent User Settings.
 * Requirement 3.7: Offline-First strategy / Manual Sync trigger.
 */
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {
    val isSyncing by viewModel.isSyncing.collectAsState()
    val darkTheme by viewModel.darkTheme.collectAsState()
    val budget by viewModel.budget.collectAsState()
    val connectivity by viewModel.connectivityStatus.collectAsState()

    var showBudgetDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val error by viewModel.error.collectAsState()

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            // General Section
            SettingsSection(title = "General") {
                SettingsClickableItem(
                    title = "Monthly Budget",
                    subtitle = if (budget > 0) String.format(Locale.getDefault(), "€%.2f", budget) else "Not set",
                    icon = Icons.Default.Timeline,
                    onClick = { showBudgetDialog = true }
                )
            }

            // Appearance Section
            SettingsSection(title = "Appearance") {
                SettingsToggleItem(
                    title = "Dark Theme",
                    subtitle = when(darkTheme) {
                        true -> "On"
                        false -> "Off"
                        null -> "System default"
                    },
                    icon = Icons.Default.DarkMode,
                    checked = darkTheme ?: false,
                    onCheckedChange = { viewModel.onDarkThemeChange(it) }
                )
            }

            // Data & Sync Section
            SettingsSection(title = "Data & Sync") {
                SettingsSyncItem(
                    isSyncing = isSyncing,
                    isConnected = connectivity == ConnectivityObserver.Status.Available,
                    onSyncClick = { viewModel.triggerManualSync() }
                )
            }
        }
    }

    if (showBudgetDialog) {
        BudgetEditDialog(
            currentBudget = budget,
            onDismiss = { showBudgetDialog = false },
            onConfirm = {
                viewModel.onBudgetChange(it)
                showBudgetDialog = false
            }
        )
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(content = content)
        }
    }
}

@Composable
fun SettingsClickableItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(subtitle) },
        leadingContent = { Icon(icon, contentDescription = null) },
        trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
        modifier = Modifier.clickable(onClick = onClick),
        colors = ListItemDefaults.colors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
    )
}

@Composable
fun SettingsToggleItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(subtitle) },
        leadingContent = { Icon(icon, contentDescription = null) },
        trailingContent = { 
            Switch(checked = checked, onCheckedChange = onCheckedChange) 
        },
        colors = ListItemDefaults.colors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
    )
}

@Composable
fun SettingsSyncItem(
    isSyncing: Boolean,
    isConnected: Boolean,
    onSyncClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text("Manual Cloud Sync") },
        supportingContent = { 
            Text(
                if (isConnected) "Force immediate synchronization" 
                else "Sync unavailable while offline"
            ) 
        },
        leadingContent = { 
            Icon(
                imageVector = Icons.Default.Sync, 
                contentDescription = null,
                tint = if (isConnected) LocalContentColor.current else MaterialTheme.colorScheme.outline
            ) 
        },
        trailingContent = {
            if (isSyncing) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
            } else {
                TextButton(
                    onClick = onSyncClick,
                    enabled = isConnected
                ) {
                    Text("SYNC NOW")
                }
            }
        },
        colors = ListItemDefaults.colors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
    )
}

@Composable
fun BudgetEditDialog(
    currentBudget: Double,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var textValue by remember { mutableStateOf(if (currentBudget > 0) currentBudget.toString() else "") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Monthly Budget") },
        text = {
            OutlinedTextField(
                value = textValue,
                onValueChange = { textValue = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = { 
                val budget = textValue.toDoubleOrNull() ?: 0.0
                onConfirm(budget) 
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

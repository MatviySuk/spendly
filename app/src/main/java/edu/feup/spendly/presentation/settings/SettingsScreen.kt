package edu.feup.spendly.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
    val currency by viewModel.currency.collectAsState()
    val darkTheme by viewModel.darkTheme.collectAsState()
    val budget by viewModel.budget.collectAsState()

    var showCurrencyDialog by remember { mutableStateOf(false) }
    var showBudgetDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                title = "Currency",
                subtitle = currency,
                icon = Icons.Default.AccountBalanceWallet,
                onClick = { showCurrencyDialog = true }
            )
            
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
                onSyncClick = { viewModel.triggerManualSync() }
            )
        }
    }

    if (showCurrencyDialog) {
        CurrencySelectionDialog(
            currentCurrency = currency,
            onDismiss = { showCurrencyDialog = false },
            onCurrencySelected = {
                viewModel.onCurrencyChange(it)
                showCurrencyDialog = false
            }
        )
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
    onSyncClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text("Manual Cloud Sync") },
        supportingContent = { Text("Force immediate synchronization") },
        leadingContent = { Icon(Icons.Default.Sync, contentDescription = null) },
        trailingContent = {
            if (isSyncing) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
            } else {
                TextButton(onClick = onSyncClick) {
                    Text("SYNC NOW")
                }
            }
        },
        colors = ListItemDefaults.colors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
    )
}

@Composable
fun CurrencySelectionDialog(
    currentCurrency: String,
    onDismiss: () -> Unit,
    onCurrencySelected: (String) -> Unit
) {
    val options = listOf("EUR", "USD", "GBP", "JPY")
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Currency") },
        text = {
            Column(Modifier.selectableGroup()) {
                options.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (text == currentCurrency),
                                onClick = { onCurrencySelected(text) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == currentCurrency),
                            onClick = null
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
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

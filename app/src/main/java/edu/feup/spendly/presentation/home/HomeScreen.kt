package edu.feup.spendly.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.feup.spendly.domain.model.Expense

/**
 * Home Screen displaying the dashboard and recent expenses.
 * Requirement 3.2: Jetpack Compose UI.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAddExpenseClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Spendly Dashboard") },
                actions = {
                    IconButton(onClick = { viewModel.syncData() }) {
                        // TODO: Add Sync Icon (Requirement 3.7)
                        Text("🔄") 
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddExpenseClick) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Balance Summary Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Total Monthly Spending",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = "${String.format("%.2f", uiState.totalBalance)}€",
                        style = MaterialTheme.typography.displayMedium
                    )
                }
            }

            Text(
                text = "Recent Transactions",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (uiState.isLoading && uiState.expenses.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.expenses.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("No expenses logged yet.")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.expenses) { expense ->
                        ExpenseItem(expense)
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseItem(expense: Expense) {
    /**
     * TODO: Create a row layout for each expense item.
     * Include amount, category, date, and location (if available).
     */
    ListItem(
        headlineContent = { Text("${expense.category}") },
        supportingContent = { Text("${expense.location ?: "No location"} • ${expense.notes ?: "No notes"}") },
        trailingContent = { 
            Text(
                text = "${String.format("%.2f", expense.amount)}€",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    )
}

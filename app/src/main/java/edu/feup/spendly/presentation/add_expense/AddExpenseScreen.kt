package edu.feup.spendly.presentation.add_expense

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Screen for Adding an Expense.
 * Requirement 3.2: Jetpack Compose UI.
 * Requirement 3.6: Location Sensor Integration.
 */
@Composable
fun AddExpenseScreen(
    viewModel: AddExpenseViewModel,
    onBack: () -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Food") }
    var notes by remember { mutableStateOf("") }
    val location by viewModel.location.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        /**
         * TODO: Implement the form layout.
         * 1. Input for Amount (NumberKeyboard).
         * 2. Dropdown/Menu for Category Selection.
         * 3. TextField for Notes.
         * 4. Display the automatically fetched location (if available).
         * 5. Button to save the expense.
         */
        
        Text(text = "Location: ${location ?: "Fetching location..."}")
        
        Button(
            onClick = {
                viewModel.saveExpense(amount.toDoubleOrNull() ?: 0.0, category, notes)
                onBack()
            }
        ) {
            Text("Save Expense")
        }
    }
}

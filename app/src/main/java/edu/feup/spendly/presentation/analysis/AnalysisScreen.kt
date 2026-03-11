package edu.feup.spendly.presentation.analysis

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Screen for Spending Analysis.
 * Requirement 3.2: Jetpack Compose UI.
 */
@Composable
fun AnalysisScreen(
    viewModel: AnalysisViewModel
) {
    val categoryTotals by viewModel.categoryTotals.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Spending Analysis",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        /**
         * TODO: Implement data visualization.
         * 1. Display a Pie Chart or Bar Chart showing spending per category.
         * 2. Use a library like Vico or Compose-Charts (Requirement 3.2).
         * 3. Show a list of totals below the chart.
         */
        categoryTotals.forEach { (category, total) ->
            ListItem(
                headlineContent = { Text(category) },
                trailingContent = { Text("${total}€") }
            )
        }
    }
}

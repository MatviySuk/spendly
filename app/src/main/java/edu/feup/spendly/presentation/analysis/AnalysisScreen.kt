package edu.feup.spendly.presentation.analysis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import edu.feup.spendly.presentation.util.CategoryColors

/**
 * Screen for Spending Analysis.
 * Requirement 3.2: Jetpack Compose UI with modern Data Visualization.
 */
@Composable
fun AnalysisScreen(
    viewModel: AnalysisViewModel
) {
    val categoryTotals by viewModel.categoryTotals.collectAsState()
    
    // Sort categories by total descending to ensure consistent ordering
    val sortedCategoryTotals = remember(categoryTotals) {
        categoryTotals.toList().sortedByDescending { it.second }
    }
    val categories = remember(sortedCategoryTotals) { sortedCategoryTotals.map { it.first } }
    val totals = remember(sortedCategoryTotals) { sortedCategoryTotals.map { it.second } }

    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(sortedCategoryTotals) {
        if (totals.isNotEmpty()) {
            modelProducer.runTransaction {
                /*
                 * We create multiple series, each with one bar, so that we can
                 * assign different colors to each bar using the series column provider.
                 * This ensures the bar color matches the category color in the list.
                 */
                columnSeries {
                    totals.forEachIndexed { index, total ->
                        series(x = listOf(index), y = listOf(total))
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Analysis",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        if (totals.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Monthly Overview",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    CartesianChartHost(
                        chart = rememberCartesianChart(
                            rememberColumnCartesianLayer(
                                ColumnCartesianLayer.ColumnProvider.series(
                                    categories.map { category ->
                                        rememberLineComponent(
                                            color = CategoryColors.getColorForCategory(category),
                                            thickness = 16.dp,
                                            shape = com.patrykandpatrick.vico.core.common.shape.Shape.rounded(40)
                                        )
                                    }
                                )
                            ),
                            startAxis = rememberStartAxis(
                                label = rememberTextComponent(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            ),
                            bottomAxis = rememberBottomAxis(
                                label = rememberTextComponent(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                valueFormatter = { value, _, _ -> 
                                    categories.getOrNull(value.toInt()) ?: "" 
                                }
                            ),
                        ),
                        modelProducer = modelProducer,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        Text(
            text = "Top Categories",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            sortedCategoryTotals.forEach { (category, total) ->
                val color = CategoryColors.getColorForCategory(category)
                
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 1.dp
                ) {
                    ListItem(
                        leadingContent = {
                            Surface(
                                modifier = Modifier.size(12.dp),
                                shape = RoundedCornerShape(4.dp),
                                color = color
                            ) {}
                        },
                        headlineContent = { Text(category, fontWeight = FontWeight.SemiBold) },
                        trailingContent = { 
                            Text(
                                text = "€${String.format("%.2f", total)}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            ) 
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }
            }
        }
    }
}

package edu.feup.spendly.presentation.util

import androidx.compose.ui.graphics.Color

object CategoryColors {
    private val chartColors = listOf(
        Color(0xFF6200EE), // Deep Purple
        Color(0xFF03DAC6), // Teal
        Color(0xFFFF0266), // Pink
        Color(0xFFFDD835), // Yellow
        Color(0xFF43A047), // Green
        Color(0xFFFB8C00), // Orange
        Color(0xFF2196F3), // Blue
        Color(0xFF9C27B0), // Purple
        Color(0xFFE91E63), // Pink
        Color(0xFFFF5722)  // Deep Orange
    )

    private val categoryMap = mapOf(
        "Food" to Color(0xFF43A047),          // Green
        "Transport" to Color(0xFF2196F3),     // Blue
        "Shopping" to Color(0xFFFB8C00),      // Orange
        "Entertainment" to Color(0xFF6200EE), // Deep Purple
        "Health" to Color(0xFFFF0266),        // Pink
        "Other" to Color(0xFF9E9E9E)          // Grey
    )

    fun getColorForCategory(category: String): Color {
        return categoryMap[category] ?: chartColors[category.hashCode().coerceAtLeast(0) % chartColors.size]
    }
}

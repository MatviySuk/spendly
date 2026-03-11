package edu.feup.spendly.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.feup.spendly.presentation.add_expense.AddExpenseScreen
import edu.feup.spendly.presentation.add_expense.AddExpenseViewModel
import edu.feup.spendly.presentation.analysis.AnalysisScreen
import edu.feup.spendly.presentation.analysis.AnalysisViewModel
import edu.feup.spendly.presentation.home.HomeScreen
import edu.feup.spendly.presentation.home.HomeViewModel
import edu.feup.spendly.presentation.settings.SettingsScreen
import edu.feup.spendly.presentation.settings.SettingsViewModel

/**
 * Routes for the application.
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddExpense : Screen("add_expense")
    object Analysis : Screen("analysis")
    object Settings : Screen("settings")
}

/**
 * Navigation Host for Spendly.
 * Requirement 3.2: Structured navigation using Compose navigation.
 */
@Composable
fun SpendlyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = viewModel,
                onAddExpenseClick = {
                    navController.navigate(Screen.AddExpense.route)
                }
            )
        }

        composable(Screen.AddExpense.route) {
            val viewModel: AddExpenseViewModel = hiltViewModel()
            AddExpenseScreen(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Analysis.route) {
            val viewModel: AnalysisViewModel = hiltViewModel()
            AnalysisScreen(
                viewModel = viewModel
            )
        }

        composable(Screen.Settings.route) {
            val viewModel: SettingsViewModel = hiltViewModel()
            SettingsScreen(
                viewModel = viewModel
            )
        }
    }
}

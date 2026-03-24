package edu.feup.spendly

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.feup.spendly.data.connectivity.ConnectivityObserver
import edu.feup.spendly.presentation.navigation.Screen
import edu.feup.spendly.presentation.navigation.SpendlyNavHost
import edu.feup.spendly.ui.theme.SpendlyTheme

/**
 * Main Activity for the Spendly application.
 * Annotated with @AndroidEntryPoint for Hilt.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
             viewModel.handleNfcIntent(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (intent != null && NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
             viewModel.handleNfcIntent(intent)
        }

        setContent {
            val darkThemePref by viewModel.darkTheme.collectAsState()
            val useDarkTheme = darkThemePref ?: isSystemInDarkTheme()

            SpendlyTheme(darkTheme = useDarkTheme) {
                val navController = rememberNavController()
                val connectivity by viewModel.connectivityStatus.collectAsState()
                
                val items = listOf(
                    BottomNavItem("Home", Screen.Home.route, Icons.Filled.Home),
                    BottomNavItem("Analysis", Screen.Analysis.route, Icons.Filled.ThumbUp),
                    BottomNavItem("Settings", Screen.Settings.route, Icons.Filled.Settings)
                )

                Scaffold(
                    topBar = {
                        if (connectivity != ConnectivityObserver.Status.Available) {
                            Surface(
                                color = MaterialTheme.colorScheme.errorContainer,
                                modifier = Modifier.fillMaxWidth().height(24.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "Offline Mode - Cloud Sync Paused",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                        }
                    },
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        
                        if (items.any { it.route == currentDestination?.route }) {
                            NavigationBar {
                                items.forEach { item ->
                                    NavigationBarItem(
                                        icon = { Icon(item.icon, contentDescription = item.name) },
                                        label = { Text(item.name) },
                                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                                        onClick = {
                                            navController.navigate(item.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    SpendlyNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector
)

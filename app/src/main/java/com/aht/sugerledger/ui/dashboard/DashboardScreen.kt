package com.aht.sugerledger.ui.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aht.sugerledger.navigation.Routes
import com.aht.sugerledger.ui.inventory.InventoryScreen
import com.aht.sugerledger.ui.orders.OrdersScreen
import com.aht.sugerledger.ui.sales.SalesScreen
import com.aht.sugerledger.ui.settings.SettingsScreen
import com.aht.sugerledger.ui.stats.StatsScreen

private data class DashboardTab(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val dashboardTabs = listOf(
    DashboardTab(Routes.SALES, "Sales", Icons.Default.PointOfSale),
    DashboardTab(Routes.INVENTORY, "Inventory", Icons.Default.Inventory2),
    DashboardTab(Routes.ORDERS, "Orders", Icons.Default.ReceiptLong),
    DashboardTab(Routes.STATS, "Stats", Icons.Default.BarChart),
    DashboardTab(Routes.SETTINGS, "Settings", Icons.Default.Settings),
)

@Composable
fun DashboardScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                dashboardTabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute == tab.route,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.SALES,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.SALES) { SalesScreen() }
            composable(Routes.INVENTORY) { InventoryScreen() }
            composable(Routes.ORDERS) { OrdersScreen() }
            composable(Routes.STATS) { StatsScreen() }
            composable(Routes.SETTINGS) { SettingsScreen() }
        }
    }
}

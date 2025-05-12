package com.komzak.wedriveevaluationassignment.presentation.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.komzak.wedriveevaluationassignment.presentation.navigation.BottomNavItem
import com.komzak.wedriveevaluationassignment.presentation.theme.greyBorderColor
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryBackground
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryColor
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryTextColor
import com.komzak.wedriveevaluationassignment.presentation.theme.secondaryBackground
import com.komzak.wedriveevaluationassignment.presentation.ui.history.HistoryScreen
import com.komzak.wedriveevaluationassignment.presentation.ui.home.HomeScreen
import com.komzak.wedriveevaluationassignment.presentation.ui.orders.OrdersScreen
import com.komzak.wedriveevaluationassignment.presentation.ui.profile.ProfileScreen

@Composable
fun DashboardScreen(
    navController: NavController
) {
    val bottomNavController = rememberNavController()
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.History,
        BottomNavItem.Orders,
        BottomNavItem.Profile
    )

    Scaffold(
        containerColor = secondaryBackground,
        contentColor = secondaryBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
            ) {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(item.iconRes),
                                contentDescription = null
                            )
                        },
                        label = { Text(item.title, style = MaterialTheme.typography.labelSmall) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            bottomNavController.navigate(item.route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = primaryColor,
                            unselectedIconColor = primaryTextColor,
                            selectedTextColor = primaryColor,
                            unselectedTextColor = primaryTextColor,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier
                .background(primaryBackground)
                .padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) { HomeScreen(navController = navController) }
            composable(BottomNavItem.History.route) { HistoryScreen() }
            composable(BottomNavItem.Orders.route) { OrdersScreen() }
            composable(BottomNavItem.Profile.route) { ProfileScreen(navController) }
        }
    }
}
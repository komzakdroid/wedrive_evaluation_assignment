package com.komzak.wedriveevaluationassignment.presentation.ui.dashboard

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.komzak.wedriveevaluationassignment.presentation.navigation.BottomNavItem
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryColor
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryTextColor
import com.komzak.wedriveevaluationassignment.presentation.ui.history.HistoryScreen
import com.komzak.wedriveevaluationassignment.presentation.ui.home.HomeScreen
import com.komzak.wedriveevaluationassignment.presentation.ui.orders.OrdersHistoryScreen
import com.komzak.wedriveevaluationassignment.presentation.ui.profile.ProfileScreen

private val modernBackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF667eea),
        Color(0xFF764ba2),
        Color(0xFFF093fb)
    )
)

fun Context.getActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}

@Composable
fun DashboardScreen(
    navController: NavController
) {
    val view = LocalView.current
    val window = view.context.getActivity()?.window

    LaunchedEffect(Unit) {
        window?.let {
            it.statusBarColor = 0xFF667eea.toInt()
            val controller = WindowCompat.getInsetsController(it, view)
            controller.isAppearanceLightStatusBars = false
        }
    }

    val bottomNavController = rememberNavController()
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.History,
        BottomNavItem.Orders,
        BottomNavItem.Profile
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(modernBackgroundGradient)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                SimpleBottomBar(
                    items = items,
                    navController = bottomNavController
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = bottomNavController,
                startDestination = BottomNavItem.Home.route,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable(BottomNavItem.Home.route) {
                    HomeScreen(navController = navController)
                }
                composable(BottomNavItem.History.route) {
                    HistoryScreen()
                }
                composable(BottomNavItem.Orders.route) {
                    OrdersHistoryScreen()
                }
                composable(BottomNavItem.Profile.route) {
                    ProfileScreen(navController)
                }
            }
        }
    }
}

@Composable
private fun SimpleBottomBar(
    items: List<BottomNavItem>,
    navController: androidx.navigation.NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentDestination?.hierarchy?.any {
                    it.route == item.route
                } == true

                SimpleNavItem(
                    item = item,
                    isSelected = isSelected,
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

@Composable
private fun SimpleNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Icon(
            painter = painterResource(item.iconRes),
            contentDescription = item.title,
            modifier = Modifier.size(20.dp),
            tint = if (isSelected) primaryColor else primaryTextColor.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = item.title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) primaryColor else primaryTextColor.copy(alpha = 0.7f)
        )
    }
}
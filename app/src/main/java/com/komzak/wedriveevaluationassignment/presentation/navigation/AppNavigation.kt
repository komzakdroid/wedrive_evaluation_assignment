package com.komzak.wedriveevaluationassignment.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.komzak.wedriveevaluationassignment.data.local.DataStoreHelper
import com.komzak.wedriveevaluationassignment.presentation.ui.createbalancerecords.CreateBalanceRecordsScreen
import com.komzak.wedriveevaluationassignment.presentation.ui.createtransaction.CreateTransactionScreen
import com.komzak.wedriveevaluationassignment.presentation.ui.dashboard.DashboardScreen
import com.komzak.wedriveevaluationassignment.presentation.ui.login.LoginScreen
import org.koin.compose.koinInject

sealed class NavRoute(val route: String) {
    companion object {
        const val LOGIN = "login"
        const val DASHBOARD = "dashboard"
        const val CREATE_BALANCE_RECORDS = "create_balance_records"
        const val CREATE_TRANSACTION = "create_transaction"
    }

    data object Login : NavRoute(LOGIN)
    data object Dashboard : NavRoute(DASHBOARD)
    data object BalanceRecords : NavRoute(CREATE_BALANCE_RECORDS)
    data object CreateTransaction : NavRoute(CREATE_TRANSACTION)
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    dataStoreHelper: DataStoreHelper = koinInject()
) {
    val navController = rememberNavController()
    val token by dataStoreHelper.getToken().collectAsStateWithLifecycle(initialValue = null)
    val startDestination =
        if (token != null) NavRoute.Dashboard.route else NavRoute.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(NavRoute.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(NavRoute.Dashboard.route) {
            DashboardScreen(navController = navController)
        }

        composable(NavRoute.BalanceRecords.route) {
            CreateBalanceRecordsScreen(navController = navController)
        }

        composable(NavRoute.CreateTransaction.route) {
            CreateTransactionScreen(navController = navController)
        }
    }
}
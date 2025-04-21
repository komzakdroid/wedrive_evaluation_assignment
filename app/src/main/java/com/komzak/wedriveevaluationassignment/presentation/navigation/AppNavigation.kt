package com.komzak.wedriveevaluationassignment.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.komzak.wedriveevaluationassignment.presentation.ui.auth.CreateUserScreen
import com.komzak.wedriveevaluationassignment.presentation.ui.wallet.WalletScreen


sealed class NavRoute(val route: String) {
    companion object {
        const val ARG_ID = "id"
        const val ARG_PHONE = "phone"
    }

    private object RouteNames {
        const val WALLET = "wallet"
        const val CREATE_USER = "create_user"
        const val ADD_CARD = "add_card"
        const val ADD_WALLET = "add_wallet"
    }

    data object Wallet : NavRoute(RouteNames.WALLET)
    data object CreateUser : NavRoute(RouteNames.CREATE_USER)
    data object AddCard : NavRoute("${RouteNames.ADD_CARD}/{$ARG_ID}")
    data object AddWallet : NavRoute("${RouteNames.ADD_WALLET}/{$ARG_ID}")

    fun AddCard.withId(id: String): String = "${RouteNames.ADD_CARD}/$id"
    fun AddWallet.withId(id: String): String = "${RouteNames.ADD_WALLET}/$id"
}


@Composable
fun AppNavigation(modifier: Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavRoute.Wallet.route) {
        composable(NavRoute.Wallet.route) {
            WalletScreen(navController = navController)
        }
        composable(NavRoute.CreateUser.route) { backStackEntry ->
            CreateUserScreen(navController = navController)
        }
        composable(NavRoute.AddCard.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString(NavRoute.ARG_ID) ?: "0"
            Text(text = "Add Card Screen for ID: $id")
        }
        composable(NavRoute.AddWallet.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString(NavRoute.ARG_ID) ?: "0"
            Text(text = "Add Wallet Screen for ID: $id")
        }
    }
}
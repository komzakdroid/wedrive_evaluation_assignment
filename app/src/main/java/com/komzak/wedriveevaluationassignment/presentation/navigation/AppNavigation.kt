package com.komzak.wedriveevaluationassignment.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.komzak.wedriveevaluationassignment.presentation.ui.addcard.AddCardScreen
import com.komzak.wedriveevaluationassignment.presentation.ui.wallet.WalletScreen


sealed class NavRoute(val route: String) {
    companion object {
        const val WALLET = "wallet"
        const val ADD_CARD = "add_card"
    }

    data object Wallet : NavRoute(WALLET)
    data object AddCard : NavRoute(ADD_CARD)
}


@Composable
fun AppNavigation(modifier: Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavRoute.Wallet.route) {
        composable(NavRoute.Wallet.route) {
            WalletScreen(navController = navController)
        }

        composable(NavRoute.AddCard.route) { backStackEntry ->
            AddCardScreen(navController)
        }
    }
}
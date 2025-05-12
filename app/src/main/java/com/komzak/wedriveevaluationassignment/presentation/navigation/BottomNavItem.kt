package com.komzak.wedriveevaluationassignment.presentation.navigation

import com.komzak.wedriveevaluationassignment.R

sealed class BottomNavItem(val route: String, val title: String, val iconRes: Int) {
    data object Home : BottomNavItem("home", "Asosiy", R.drawable.ic_home)
    data object History : BottomNavItem("history", "Tarix", R.drawable.ic_history)
    data object Orders : BottomNavItem("orders", "Buyurtmalar", R.drawable.ic_order)
    data object Profile : BottomNavItem("profile", "Profil", R.drawable.ic_profile)
}
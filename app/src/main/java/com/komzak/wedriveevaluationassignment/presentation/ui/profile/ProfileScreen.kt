package com.komzak.wedriveevaluationassignment.presentation.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.komzak.wedriveevaluationassignment.data.local.DataStoreHelper
import com.komzak.wedriveevaluationassignment.presentation.navigation.NavRoute
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryBackground
import com.komzak.wedriveevaluationassignment.presentation.theme.whiteColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun ProfileScreen(
    navController: NavController,
    dataStoreHelper: DataStoreHelper = koinInject()
) {
    val phoneNumber by dataStoreHelper.getPhoneNumber().collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium,
            color = whiteColor
        )
        Text(
            text = "Phone: ${phoneNumber ?: "Loading..."}",
            style = MaterialTheme.typography.bodyLarge,
            color = whiteColor,
            modifier = Modifier.padding(top = 16.dp)
        )
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    dataStoreHelper.clearCache()
                    // Navigate on the main thread
                    CoroutineScope(Dispatchers.Main).launch {
                        navController.navigate(NavRoute.Login.route) {
                            popUpTo(0) { inclusive = true } // Clear the entire back stack
                            launchSingleTop = true
                        }
                    }
                }
            },
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text("Logout")
        }
    }
}
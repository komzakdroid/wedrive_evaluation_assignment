package com.komzak.wedriveevaluationassignment.presentation.ui.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryBackground
import com.komzak.wedriveevaluationassignment.presentation.theme.whiteColor

@Composable
fun OrdersScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Current Orders",
            style = MaterialTheme.typography.headlineMedium,
            color = whiteColor
        )
        Text(
            text = "No active orders",
            style = MaterialTheme.typography.bodyLarge,
            color = whiteColor,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
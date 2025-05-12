package com.komzak.wedriveevaluationassignment.presentation.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.komzak.wedriveevaluationassignment.R
import com.komzak.wedriveevaluationassignment.domain.model.BalanceModel
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryColor
import com.komzak.wedriveevaluationassignment.presentation.theme.secondaryBackground
import com.komzak.wedriveevaluationassignment.presentation.theme.whiteColor
import com.valentinilk.shimmer.shimmer
import org.koin.compose.viewmodel.koinViewModel

// Constants for consistent styling
private object HomeScreenConstants {
    val ScreenPadding = 16.dp
    val CardPadding = 8.dp
    val CardCornerRadius = 16.dp
    val AvatarSize = 48.dp
    val FabSize = 56.dp
    val SpacingSmall = 8.dp
    val SpacingMedium = 16.dp
    val SpacingLarge = 24.dp
}

// Gradient background for the screen
private val gradientBackground = Brush.verticalGradient(
    colors = listOf(
        secondaryBackground,
        secondaryBackground.copy(alpha = 0.8f)
    )
)

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    navController: NavController
) {
    var showDialog by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(
        uiState = uiState,
        onAddBalanceClick = { showDialog = true },
        navController = navController,
        showDialog = showDialog,
        onDismissDialog = { showDialog = false }
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onAddBalanceClick: () -> Unit,
    navController: NavController,
    showDialog: Boolean,
    onDismissDialog: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(HomeScreenConstants.ScreenPadding)
        ) {
            // Header with profile and logout
            HomeHeader(phone = uiState.phone)
            Spacer(modifier = Modifier.height(HomeScreenConstants.SpacingLarge))

            // Balance list
            BalanceList(
                isLoading = uiState.isLoading,
                balances = uiState.allBalances
            )
        }

        // Floating Action Button for adding balance
        FloatingActionButton(
            onClick = onAddBalanceClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(HomeScreenConstants.ScreenPadding),
            shape = CircleShape,
            containerColor = primaryColor,
            contentColor = whiteColor
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = "Add Balance",
                modifier = Modifier.size(24.dp)
            )
        }

        // Dialog for selecting action
        if (showDialog) {
            SelectionDialog(
                onDismiss = onDismissDialog,
                onCreateOrder = { navController.navigate("create_order") },
                onCreateExchange = { navController.navigate("create_exchange") }
            )
        }
    }
}

@Composable
private fun SelectionDialog(
    onDismiss: () -> Unit,
    onCreateOrder: () -> Unit,
    onCreateExchange: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = { Text(text = "Davom etish uchun tanlang:") },
        confirmButton = {
            Column(horizontalAlignment = Alignment.End) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onCreateOrder()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text("Savdo", color = whiteColor)
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onCreateExchange()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text("Yetkazib berish", color = whiteColor)
                }
            }
        }
    )
}

@Composable
private fun HomeHeader(phone: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HomeScreenConstants.SpacingSmall)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_hisobchi),
                contentDescription = "Profile Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(2.dp)
                    .size(HomeScreenConstants.AvatarSize)
            )
            Column {
                Text(
                    text = "HISOBCHI",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
                Text(
                    text = "Profil: $phone",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    color = primaryColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun BalanceList(
    isLoading: Boolean,
    balances: List<BalanceModel>
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = HomeScreenConstants.FabSize + HomeScreenConstants.ScreenPadding * 2),
        verticalArrangement = Arrangement.spacedBy(HomeScreenConstants.CardPadding)
    ) {
        if (isLoading) {
            items(5) {
                BalanceBoxShimmer()
            }
        } else {
            items(balances) { balance ->
                BalanceBox(balance = balance)
            }
        }
    }
}

@Composable
private fun BalanceBoxShimmer() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(HomeScreenConstants.CardPadding)
            .shimmer(),
        shape = RoundedCornerShape(HomeScreenConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = whiteColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(HomeScreenConstants.SpacingMedium),
            verticalArrangement = Arrangement.spacedBy(HomeScreenConstants.SpacingSmall),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = " ".repeat(20),
                style = MaterialTheme.typography.bodyLarge,
                color = primaryColor.copy(alpha = 0.3f)
            )
            Text(
                text = " ".repeat(15),
                style = MaterialTheme.typography.bodyLarge,
                color = primaryColor.copy(alpha = 0.3f)
            )
            Text(
                text = " ".repeat(10),
                style = MaterialTheme.typography.bodyLarge,
                color = primaryColor.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
private fun BalanceBox(balance: BalanceModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(HomeScreenConstants.CardPadding),
        shape = RoundedCornerShape(HomeScreenConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = whiteColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            whiteColor,
                            whiteColor.copy(alpha = 0.95f)
                        )
                    )
                )
                .padding(HomeScreenConstants.SpacingMedium),
            verticalArrangement = Arrangement.spacedBy(HomeScreenConstants.SpacingSmall),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HomeScreenConstants.SpacingSmall)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_balance),
                    contentDescription = "Balance Icon",
                    modifier = Modifier.size(20.dp),
                    tint = primaryColor
                )
                Text(
                    text = "Balans: ${balance.balance} ${balance.currencyType}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryColor
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HomeScreenConstants.SpacingSmall)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_incoming),
                    contentDescription = "Incoming Icon",
                    modifier = Modifier.size(20.dp),
                    tint = primaryColor
                )
                Text(
                    text = "Kirim: ${balance.outInLay} ${balance.currencyType}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = primaryColor.copy(alpha = 0.8f)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HomeScreenConstants.SpacingSmall)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_outgoing),
                    contentDescription = "Outgoing Icon",
                    modifier = Modifier.size(20.dp),
                    tint = primaryColor
                )
                Text(
                    text = "Chiqim: ${balance.inOutLay} ${balance.currencyType}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = primaryColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}
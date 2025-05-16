package com.komzak.wedriveevaluationassignment.presentation.ui.home

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.komzak.wedriveevaluationassignment.R
import com.komzak.wedriveevaluationassignment.domain.model.BalanceModel
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryColor
import com.komzak.wedriveevaluationassignment.presentation.theme.secondaryBackground
import com.komzak.wedriveevaluationassignment.presentation.theme.whiteColor
import com.valentinilk.shimmer.shimmer
import org.koin.compose.viewmodel.koinViewModel

object ScreenConstants {
    val ScreenPadding = 16.dp
    val CardPadding = 8.dp
    val CardCornerRadius = 16.dp
    val AvatarSize = 48.dp
    val FabSize = 56.dp
    val SpacingSmall = 8.dp
    val SpacingMedium = 16.dp
    val SpacingLarge = 24.dp
}

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
    // Access the current View to get the Window
    val view = LocalView.current
    val window = view.context.getActivity()?.window

    // Change the status bar color
    LaunchedEffect(Unit) {
        window?.let {
            // Set the status bar color to match the background (#4A90E2)
            it.statusBarColor = 0xFF4A90E2.toInt()

            // Make the status bar icons light (white) to be visible on the blue background
            val controller = WindowCompat.getInsetsController(it, view)
            controller.isAppearanceLightStatusBars =
                false // Dark icons for light background; set to false for light icons on dark background
        }
    }

    var showDialog by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(
        uiState = uiState,
        onAddBalanceClick = { showDialog = true },
        navController = navController,
        showDialog = showDialog,
        onDismissDialog = { showDialog = false },
        onRefresh = { viewModel.refresh() },
        check = { viewModel.createTransaction() },
    )
}

// Utility extension to get the Activity from a Context
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onAddBalanceClick: () -> Unit,
    navController: NavController,
    showDialog: Boolean,
    onDismissDialog: () -> Unit,
    onRefresh: () -> Unit,
    check: () -> Unit
) {
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoading) {
        isRefreshing = uiState.isLoading
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            onRefresh()
        },
        state = rememberPullToRefreshState(),
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4A90E2)) // Solid blue background to match the image
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF4A90E2)) // Solid blue background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(ScreenConstants.ScreenPadding)
            ) {
                HomeHeader(
                    phone = uiState.phone, showRefreshButton = uiState.errorMessage != null,
                    onRefresh = onRefresh
                )
                Spacer(modifier = Modifier.height(ScreenConstants.SpacingLarge))

                BalanceList(
                    isLoading = uiState.isLoading,
                    balances = uiState.allBalances
                )

                if (uiState.errorMessage != null) {
                    Text(
                        text = uiState.errorMessage ?: "Unknown error",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(ScreenConstants.SpacingMedium)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }

            FloatingActionButton(
                onClick = onAddBalanceClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(ScreenConstants.ScreenPadding),
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

            if (showDialog) {
                SelectionDialog(
                    onDismiss = onDismissDialog,
                    onCreateOrder = { navController.navigate("create_order") },
                    onCreateExchange = { /*navController.navigate("create_exchange")*/ check() }
                )
            }
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
private fun HomeHeader(
    phone: String, showRefreshButton: Boolean,
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(ScreenConstants.ScreenPadding)
            .padding(ScreenConstants.SpacingMedium),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ScreenConstants.SpacingSmall)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_hisobchi),
                    contentDescription = "Profile Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(2.dp)
                        .size(ScreenConstants.AvatarSize)
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

            if (showRefreshButton) {
                Icon(
                    painter = painterResource(R.drawable.ic_refresh),
                    contentDescription = "Retry",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onRefresh() },
                    tint = primaryColor
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
        contentPadding = PaddingValues(bottom = ScreenConstants.FabSize + ScreenConstants.ScreenPadding * 2),
        verticalArrangement = Arrangement.spacedBy(ScreenConstants.CardPadding)
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
            .padding(ScreenConstants.CardPadding)
            .shimmer(),
        shape = RoundedCornerShape(ScreenConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = whiteColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = " ".repeat(15),
                style = MaterialTheme.typography.bodyMedium,
                color = primaryColor.copy(alpha = 0.3f)
            )
            Text(
                text = " ".repeat(12),
                style = MaterialTheme.typography.bodySmall,
                color = primaryColor.copy(alpha = 0.3f)
            )
            Text(
                text = " ".repeat(12),
                style = MaterialTheme.typography.bodySmall,
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
            .padding(ScreenConstants.CardPadding),
        shape = RoundedCornerShape(ScreenConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = whiteColor),
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
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_balance),
                    contentDescription = "Balance Icon",
                    modifier = Modifier.size(16.dp),
                    tint = primaryColor
                )
                Text(
                    text = "Balance: ${balance.balance} ${balance.currencyType}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_incoming),
                    contentDescription = "Income Icon",
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF4CAF50) // Green for income
                )
                Text(
                    text = "Income: +${balance.outInLay} ${balance.currencyType}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF4CAF50),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_outgoing),
                    contentDescription = "Outcome Icon",
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFFF44336) // Red for outcome
                )
                Text(
                    text = "Outcome: −${balance.inOutLay} ${balance.currencyType}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFF44336),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
package com.komzak.wedriveevaluationassignment.presentation.ui.home

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.komzak.wedriveevaluationassignment.R
import com.komzak.wedriveevaluationassignment.domain.model.BalanceModel
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryColor
import com.komzak.wedriveevaluationassignment.presentation.ui.history.ModernHistoryConstants
import com.valentinilk.shimmer.shimmer
import org.koin.compose.viewmodel.koinViewModel

object ModernScreenConstants {
    val ScreenPadding = 20.dp
    val CardPadding = 12.dp
    val CardCornerRadius = 20.dp
    val LargeCardCornerRadius = 24.dp
    val AvatarSize = 56.dp
    val FabSize = 64.dp
    val SpacingTiny = 4.dp
    val SpacingSmall = 8.dp
    val SpacingMedium = 16.dp
    val SpacingLarge = 24.dp
    val SpacingXLarge = 32.dp
}

// Modern gradient backgrounds
private val modernBackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF667eea),
        Color(0xFF764ba2),
        Color(0xFFF093fb)
    )
)

private val glassmorphismBrush = Brush.linearGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.25f),
        Color.White.copy(alpha = 0.1f)
    )
)

private val cardGradient = Brush.linearGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.9f),
        Color.White.copy(alpha = 0.7f)
    )
)

data class QuickAction(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val action: () -> Unit
)

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
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

    var showDialog by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ModernHomeContent(
        uiState = uiState,
        onAddBalanceClick = { showDialog = true },
        navController = navController,
        showDialog = showDialog,
        onDismissDialog = { showDialog = false },
        onRefresh = { viewModel.refresh() },
        check = { viewModel.createTransaction() },
    )
}

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun ModernHomeContent(
    uiState: HomeUiState,
    onAddBalanceClick: () -> Unit,
    navController: NavController,
    showDialog: Boolean,
    onDismissDialog: () -> Unit,
    onRefresh: () -> Unit,
    check: () -> Unit
) {
    var isRefreshing by remember { mutableStateOf(false) }
    var selectedQuickAction by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(uiState.isLoading) {
        isRefreshing = uiState.isLoading
    }

    val quickActions = listOf(
        QuickAction("New Order", Icons.Default.ShoppingCart, Color(0xFF4CAF50)) {
            navController.navigate("create_order")
        },
        QuickAction("Delivery", Icons.Default.ShoppingCart, Color(0xFF2196F3), check),
        QuickAction("Analytics", Icons.Default.ShoppingCart, Color(0xFFFF9800)) { },
        QuickAction("Reports", Icons.Default.ShoppingCart, Color(0xFF9C27B0)) { }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(modernBackgroundGradient)
    ) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                onRefresh()
            },
            state = rememberPullToRefreshState(),
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = ModernScreenConstants.ScreenPadding,
                    end = ModernScreenConstants.ScreenPadding,
                    top = ModernScreenConstants.SpacingXLarge,
                    bottom = ModernScreenConstants.FabSize + ModernScreenConstants.ScreenPadding * 2
                ),
                verticalArrangement = Arrangement.spacedBy(ModernScreenConstants.SpacingLarge)
            ) {
                item {
                    ModernHistoryHeader(phone = uiState.phone)
                }

                item {
                    QuickActionsSection(
                        actions = quickActions,
                        selectedAction = selectedQuickAction,
                        onActionClick = { index ->
                            selectedQuickAction = index
                            quickActions[index].action()
                        }
                    )
                }

                item {
                    BalanceOverviewCard(balances = uiState.allBalances) // Here we need horizontal pager for balances in different currencies
                }

                item {
                    Text(
                        text = "Balance Histories",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = ModernScreenConstants.SpacingSmall)
                    )
                }

                if (uiState.isLoading) {
                    items(5) {
                        ModernBalanceCardShimmer()
                    }
                } else {
                    items(uiState.allBalances) { balance -> // here we don't shoe all balances , we show get history by balance id
                        ModernBalanceCard(
                            balance = balance,
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                }

                if (uiState.errorMessage != null) {
                    item {
                        ErrorCard(errorMessage = uiState.errorMessage)
                    }
                }
            }
        }

        // Modern FAB with animation
        AnimatedVisibility(
            visible = !isRefreshing,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(ModernScreenConstants.ScreenPadding)
        ) {
            FloatingActionButton(
                onClick = onAddBalanceClick,
                modifier = Modifier.size(ModernScreenConstants.FabSize),
                shape = CircleShape,
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Balance",
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        if (showDialog) {
            ModernSelectionDialog(
                onDismiss = onDismissDialog,
                onCreateOrder = { navController.navigate("create_order") },
                onCreateExchange = check
            )
        }
    }
}

private val glassmorphismCard = Brush.linearGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.25f),
        Color.White.copy(alpha = 0.1f)
    )
)

@Composable
private fun ModernHistoryHeader(phone: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = ModernHistoryConstants.SpacingMedium),
        shape = RoundedCornerShape(ModernHistoryConstants.LargeCardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(glassmorphismCard)
                .padding(ModernHistoryConstants.SpacingLarge)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "HISOBCHI",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Profile: $phone",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(ModernHistoryConstants.AvatarSize)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.3f),
                                    Color.White.copy(alpha = 0.1f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_hisobchi),
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActionsSection(
    actions: List<QuickAction>,
    selectedAction: Int?,
    onActionClick: (Int) -> Unit
) {
    Column {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = ModernScreenConstants.SpacingSmall)
        )

        Spacer(modifier = Modifier.height(ModernScreenConstants.SpacingMedium))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(ModernScreenConstants.SpacingMedium),
            contentPadding = PaddingValues(horizontal = ModernScreenConstants.SpacingSmall)
        ) {
            items(actions.size) { index ->
                val action = actions[index]
                val isSelected = selectedAction == index
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 0.95f else 1f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                )

                Card(
                    modifier = Modifier
                        .scale(scale)
                        .width(120.dp)
                        .clickable { onActionClick(index) },
                    shape = RoundedCornerShape(ModernScreenConstants.CardCornerRadius),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(glassmorphismBrush)
                            .padding(ModernScreenConstants.SpacingMedium),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(ModernScreenConstants.SpacingSmall)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(action.color.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = action.icon,
                                    contentDescription = action.title,
                                    tint = action.color,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Text(
                                text = action.title,
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BalanceOverviewCard(balances: List<BalanceModel>) {
    val totalBalance = balances.sumOf { it.balance ?: 0.0 }
    val totalIncome = balances.sumOf { it.outInLay ?: 0.0 }
    val totalOutcome = balances.sumOf { it.inOutLay ?: 0.0 }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ModernScreenConstants.LargeCardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardGradient)
                .padding(ModernScreenConstants.SpacingLarge)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(ModernScreenConstants.SpacingMedium)
            ) {
                Text(
                    text = "Balance Overview",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OverviewItem(
                        title = "Total Balance",
                        value = "${String.format("%.2f", totalBalance)}",
                        icon = Icons.Default.ShoppingCart,
                        color = Color(0xFF2196F3)
                    )
                    OverviewItem(
                        title = "Income",
                        value = "+${String.format("%.2f", totalIncome)}",
                        icon = Icons.Default.KeyboardArrowUp,
                        color = Color(0xFF4CAF50)
                    )
                    OverviewItem(
                        title = "Outcome",
                        value = "-${String.format("%.2f", totalOutcome)}",
                        icon = Icons.Default.KeyboardArrowDown,
                        color = Color(0xFFF44336)
                    )
                }
            }
        }
    }
}

@Composable
private fun OverviewItem(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ModernScreenConstants.SpacingSmall)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Black.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
private fun ModernBalanceCard(
    balance: BalanceModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ModernScreenConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardGradient)
                .padding(ModernScreenConstants.SpacingMedium)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(ModernScreenConstants.SpacingSmall)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(ModernScreenConstants.SpacingSmall)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(primaryColor.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_balance),
                                contentDescription = "Balance",
                                modifier = Modifier.size(16.dp),
                                tint = primaryColor
                            )
                        }
                        Text(
                            text = "${balance.balance ?: 0.0} ${balance.currencyType ?: ""}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = Color.Black.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Divider(
                    color = Color.Black.copy(alpha = 0.1f),
                    thickness = 1.dp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TransactionItem(
                        label = "Income",
                        amount = "+${balance.outInLay}",
                        currency = balance.currencyType.toString(),
                        icon = Icons.Default.ShoppingCart,
                        color = Color(0xFF4CAF50)
                    )
                    TransactionItem(
                        label = "Outcome",
                        amount = "-${balance.inOutLay}",
                        currency = balance.currencyType.toString(),
                        icon = Icons.Default.ShoppingCart,
                        color = Color(0xFFF44336)
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionItem(
    label: String,
    amount: String,
    currency: String,
    icon: ImageVector,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ModernScreenConstants.SpacingTiny)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(14.dp),
            tint = color
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Black.copy(alpha = 0.6f)
            )
            Text(
                text = "$amount $currency",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = color
            )
        }
    }
}

@Composable
private fun ModernBalanceCardShimmer() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shimmer(),
        shape = RoundedCornerShape(ModernScreenConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardGradient)
                .padding(ModernScreenConstants.SpacingMedium)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(ModernScreenConstants.SpacingSmall)
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.1f))
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorCard(errorMessage: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ModernScreenConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ModernScreenConstants.SpacingMedium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ModernScreenConstants.SpacingSmall)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error",
                tint = Color(0xFFFF9800),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFE65100)
            )
        }
    }
}

@Composable
private fun ModernSelectionDialog(
    onDismiss: () -> Unit,
    onCreateOrder: () -> Unit,
    onCreateExchange: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(ModernScreenConstants.LargeCardCornerRadius),
        containerColor = Color.White,
        title = {
            Text(
                text = "Choose Action",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )
        },
        text = {
            Text(
                text = "What would you like to do?",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black.copy(alpha = 0.7f)
            )
        },
        confirmButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(ModernScreenConstants.SpacingSmall)
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onCreateOrder()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(ModernScreenConstants.CardCornerRadius)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(ModernScreenConstants.SpacingSmall))
                    Text("Create Order", color = Color.White)
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onCreateExchange()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                    shape = RoundedCornerShape(ModernScreenConstants.CardCornerRadius)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(ModernScreenConstants.SpacingSmall))
                    Text("Delivery Service", color = Color.White)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = primaryColor)
            }
        }
    )
}
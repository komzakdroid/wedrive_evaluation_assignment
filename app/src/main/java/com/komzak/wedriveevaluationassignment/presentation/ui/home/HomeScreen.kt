package com.komzak.wedriveevaluationassignment.presentation.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.komzak.wedriveevaluationassignment.R
import com.komzak.wedriveevaluationassignment.domain.model.BalanceModel
import com.komzak.wedriveevaluationassignment.domain.model.BalanceRecordModel
import com.komzak.wedriveevaluationassignment.presentation.navigation.NavRoute
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryColor
import com.komzak.wedriveevaluationassignment.presentation.ui.dashboard.getActivity
import com.valentinilk.shimmer.shimmer
import org.koin.compose.viewmodel.koinViewModel
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

// Optimized constants for compact design
object CompactScreenConstants {
    val ScreenPadding = 16.dp
    val CardPadding = 12.dp
    val CompactCardPadding = 10.dp
    val CardCornerRadius = 12.dp
    val LargeCardCornerRadius = 16.dp
    val AvatarSize = 40.dp
    val CompactAvatarSize = 32.dp
    val FabSize = 56.dp
    val SpacingTiny = 2.dp
    val SpacingSmall = 4.dp
    val SpacingMedium = 8.dp
    val SpacingLarge = 12.dp
    val SpacingXLarge = 16.dp
}

// Date formatting function
private fun formatTransactionDate(isoDate: String?): String {
    if (isoDate.isNullOrEmpty()) return ""

    return try {
        // Handle ISO 8601 format (2025-05-22T23:18:01Z)
        if (isoDate.contains("T")) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                // Use modern date-time API for API 26+
                val zonedDateTime = ZonedDateTime.parse(isoDate)
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                zonedDateTime.format(formatter)
            } else {
                // Fallback for older Android versions
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                val date = inputFormat.parse(isoDate)
                date?.let { outputFormat.format(it) } ?: isoDate
            }
        } else {
            // Handle simple date format if needed
            isoDate
        }
    } catch (e: Exception) {
        // Return original string if parsing fails
        isoDate
    }
}

// Refined gradient backgrounds
private val compactBackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF667eea),
        Color(0xFF764ba2)
    )
)

private val glassmorphismBrush = Brush.linearGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.2f),
        Color.White.copy(alpha = 0.05f)
    )
)

private val cardGradient = Brush.linearGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.95f),
        Color.White.copy(alpha = 0.85f)
    )
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

    CompactHomeContent(
        uiState = uiState,
        onAddBalanceClick = { showDialog = true },
        navController = navController,
        showDialog = showDialog,
        onDismissDialog = { showDialog = false },
        onRefresh = { viewModel.refresh() },
        check = { viewModel.createTransaction() },
        onBalanceSelected = { balanceId -> viewModel.selectBalance(balanceId) }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun CompactHomeContent(
    uiState: HomeUiState,
    onAddBalanceClick: () -> Unit,
    navController: NavController,
    showDialog: Boolean,
    onDismissDialog: () -> Unit,
    onRefresh: () -> Unit,
    check: () -> Unit,
    onBalanceSelected: (Int) -> Unit
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { uiState.allBalances.size })

    val currentPage by remember {
        derivedStateOf { pagerState.currentPage }
    }

    LaunchedEffect(uiState.isLoading) {
        isRefreshing = uiState.isLoading
    }

    LaunchedEffect(currentPage, uiState.allBalances) {
        if (uiState.allBalances.isNotEmpty() && currentPage < uiState.allBalances.size) {
            val selectedBalance = uiState.allBalances[currentPage]
            selectedBalance.id?.let { balanceId ->
                if (uiState.selectedBalanceId != balanceId) {
                    onBalanceSelected(balanceId)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(compactBackgroundGradient)
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
                    start = CompactScreenConstants.ScreenPadding,
                    end = CompactScreenConstants.ScreenPadding,
                    top = CompactScreenConstants.SpacingLarge,
                    bottom = CompactScreenConstants.FabSize + CompactScreenConstants.ScreenPadding * 2
                ),
                verticalArrangement = Arrangement.spacedBy(CompactScreenConstants.SpacingMedium)
            ) {
                item {
                    CompactHeader(phone = uiState.phone)
                }

                item {
                    CompactBalancePagerSection(
                        balances = uiState.allBalances,
                        pagerState = pagerState,
                        isLoading = uiState.isLoading
                    )
                }

                item {
                    Text(
                        text = "Охирги операциялар",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = CompactScreenConstants.SpacingSmall)
                    )
                }

                if (uiState.isLoading) {
                    items(3) {
                        CompactBalanceRecordShimmer()
                    }
                } else {
                    val selectedBalanceRecords = uiState.selectedBalanceId?.let {
                        uiState.balanceRecords[it]
                    } ?: emptyList()

                    if (selectedBalanceRecords.isEmpty()) {
                        item {
                            CompactEmptyStateCard()
                        }
                    } else {
                        items(selectedBalanceRecords) { record ->
                            EnhancedBalanceRecordCard(
                                record = record,
                                modifier = Modifier.animateItemPlacement(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                            )
                        }
                    }
                }

                if (uiState.errorMessage != null) {
                    item {
                        CompactErrorCard(errorMessage = uiState.errorMessage)
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = !isRefreshing,
            enter = scaleIn(
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            ) + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(CompactScreenConstants.ScreenPadding)
        ) {
            FloatingActionButton(
                onClick = onAddBalanceClick,
                modifier = Modifier.size(CompactScreenConstants.FabSize),
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
                    contentDescription = "Қўшиш",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        if (showDialog) {
            CompactSelectionDialog(
                onDismiss = onDismissDialog,
                onCreateOrder = { navController.navigate(NavRoute.BalanceRecords.route) },
                onCreateExchange = { navController.navigate(NavRoute.CreateTransaction.route) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CompactBalancePagerSection(
    balances: List<BalanceModel>,
    pagerState: androidx.compose.foundation.pager.PagerState,
    isLoading: Boolean
) {
    Column {
        if (isLoading || balances.isEmpty()) {
            CompactBalanceCardShimmer()
        } else {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = CompactScreenConstants.SpacingLarge),
                pageSpacing = CompactScreenConstants.SpacingSmall
            ) { page ->
                val balance = balances[page]
                CompactBalanceOverviewCard(
                    balance = balance,
                    isSelected = page == pagerState.currentPage
                )
            }

            if (balances.size > 1) {
                Spacer(modifier = Modifier.height(CompactScreenConstants.SpacingSmall))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(balances.size) { index ->
                        val isSelected = index == pagerState.currentPage
                        val animatedSize by animateFloatAsState(
                            targetValue = if (isSelected) 6f else 4f,
                            animationSpec = tween(300)
                        )
                        val animatedAlpha by animateFloatAsState(
                            targetValue = if (isSelected) 1f else 0.5f,
                            animationSpec = tween(300)
                        )

                        Box(
                            modifier = Modifier
                                .size(animatedSize.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = animatedAlpha))
                        )
                        if (index < balances.size - 1) {
                            Spacer(modifier = Modifier.width(CompactScreenConstants.SpacingTiny))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CompactBalanceOverviewCard(
    balance: BalanceModel,
    isSelected: Boolean = false
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.95f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    val netAmount = (balance.outInLay ?: 0.0) - (balance.inOutLay ?: 0.0)
    val isPositiveNet = netAmount >= 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        shape = RoundedCornerShape(CompactScreenConstants.LargeCardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 12.dp else 6.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardGradient)
                .padding(CompactScreenConstants.CardPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(CompactScreenConstants.SpacingMedium)
            ) {
                // Compact Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = balance.currencyType ?: "Номаълум",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )
                        Text(
                            text = "ID: ${balance.id ?: "Йўқ"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black.copy(alpha = 0.5f)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = String.format("%.2f", balance.balance ?: 0.0),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = primaryColor
                        )
                        Text(
                            text = "Баланс",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black.copy(alpha = 0.6f)
                        )
                    }
                }

                Divider(
                    color = Color.Black.copy(alpha = 0.1f),
                    thickness = 1.dp
                )

                // Compact Financial Overview
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CompactOverviewItem(
                        title = "Даромад",
                        value = String.format("%.0f", balance.outInLay ?: 0.0),
                        icon = Icons.Default.KeyboardArrowUp,
                        color = Color(0xFF4CAF50)
                    )
                    CompactOverviewItem(
                        title = "Харажат",
                        value = String.format("%.0f", balance.inOutLay ?: 0.0),
                        icon = Icons.Default.KeyboardArrowDown,
                        color = Color(0xFFF44336)
                    )
                    CompactOverviewItem(
                        title = "Фарқ",
                        value = String.format("%.0f", netAmount),
                        icon = if (isPositiveNet) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        color = if (isPositiveNet) Color(0xFF4CAF50) else Color(0xFFF44336)
                    )
                }
            }
        }
    }
}

@Composable
private fun CompactOverviewItem(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(CompactScreenConstants.SpacingTiny)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = Color.Black.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
private fun EnhancedBalanceRecordCard(
    record: BalanceRecordModel,
    modifier: Modifier = Modifier
) {
    val isPositive = record.type == 1

    val transactionType = when (record.type) {
        1 -> "Даромад"
        2 -> "Харажат"
        else -> if (isPositive) "Кирим" else "Чиқим"
    }

    // Format date if available
    val formattedDate = formatTransactionDate(record.createdAt)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CompactScreenConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CompactScreenConstants.CardPadding)
        ) {
            // Main content row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(CompactScreenConstants.SpacingMedium),
                    modifier = Modifier.weight(1f)
                ) {
                    // Transaction icon
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                if (isPositive) Color(0xFF4CAF50).copy(alpha = 0.15f)
                                else Color(0xFFF44336).copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPositive) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = transactionType,
                            tint = if (isPositive) Color(0xFF4CAF50) else Color(0xFFF44336),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Transaction details
                    Column(
                        verticalArrangement = Arrangement.spacedBy(CompactScreenConstants.SpacingTiny),
                        modifier = Modifier.weight(1f)
                    ) {
                        // Main description
                        Text(
                            text = record.details ?: "Операция тафсилоти",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 15.sp,
                                lineHeight = 20.sp
                            ),
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black.copy(alpha = 0.9f),
                            maxLines = 5,
                            overflow = TextOverflow.Ellipsis
                        )

                        // Transaction type badge
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isPositive) Color(0xFF4CAF50).copy(alpha = 0.1f)
                                    else Color(0xFFF44336).copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = transactionType,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium,
                                color = if (isPositive) Color(0xFF4CAF50) else Color(0xFFF44336)
                            )
                        }
                    }
                }

                // Amount column
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(CompactScreenConstants.SpacingTiny)
                ) {
                    Text(
                        text = "${if (isPositive) "+" else "-"}${record.amount}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp
                        ),
                        fontWeight = FontWeight.Bold,
                        color = if (isPositive) Color(0xFF4CAF50) else Color(0xFFF44336)
                    )
                    Text(
                        text = record.currencyType ?: "СЎМ",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Black.copy(alpha = 0.6f)
                    )
                }
            }

            // Additional info row
            if (formattedDate.isNotEmpty() || record.serialNo != null) {
                Spacer(modifier = Modifier.height(CompactScreenConstants.SpacingSmall))
                HorizontalDivider(
                    color = Color.Black.copy(alpha = 0.05f),
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(CompactScreenConstants.SpacingSmall))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Date and time
                    if (formattedDate.isNotEmpty()) {
                        Text(
                            text = formattedDate,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black.copy(alpha = 0.5f)
                        )
                    }

                    // Serial number
                    /*  record.serialNo?.let { serial ->
                          Text(
                              text = "№ $serial",
                              style = MaterialTheme.typography.bodySmall,
                              fontWeight = FontWeight.Medium,
                              color = Color.Black.copy(alpha = 0.5f)
                          )
                      }*/
                }
            }
        }
    }
}

@Composable
private fun CompactBalanceCardShimmer() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shimmer(),
        shape = RoundedCornerShape(CompactScreenConstants.LargeCardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(CompactScreenConstants.CardPadding)
        ) {
            // Shimmer content placeholder
        }
    }
}

@Composable
private fun CompactBalanceRecordShimmer() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shimmer(),
        shape = RoundedCornerShape(CompactScreenConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(CompactScreenConstants.CompactCardPadding)
        ) {
            // Shimmer content placeholder
        }
    }
}

@Composable
private fun CompactEmptyStateCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CompactScreenConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CompactScreenConstants.SpacingLarge),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(CompactScreenConstants.SpacingSmall)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Операциялар йўқ",
                    tint = Color.Black.copy(alpha = 0.4f),
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = "Ҳали операциялар йўқ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black.copy(alpha = 0.7f)
                )
                Text(
                    text = "Операцияларингиз шу ерда кўринади",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun CompactHeader(phone: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CompactScreenConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(glassmorphismBrush)
                .padding(CompactScreenConstants.CardPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ҲИСОБЧИ",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = phone,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(CompactScreenConstants.AvatarSize)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_hisobchi),
                        contentDescription = "Профиль",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CompactErrorCard(errorMessage: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CompactScreenConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CompactScreenConstants.CompactCardPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(CompactScreenConstants.SpacingSmall)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Хатолик",
                tint = Color(0xFFFF9800),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFE65100),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CompactSelectionDialog(
    onDismiss: () -> Unit,
    onCreateOrder: () -> Unit,
    onCreateExchange: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(CompactScreenConstants.LargeCardCornerRadius),
        containerColor = Color.White,
        title = {
            Text(
                text = "Амални танланг",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )
        },
        text = {
            Text(
                text = "Қандай амал бажармоқчисиз?",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black.copy(alpha = 0.7f)
            )
        },
        confirmButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(CompactScreenConstants.SpacingSmall)
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onCreateOrder()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(CompactScreenConstants.CardCornerRadius)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(CompactScreenConstants.SpacingSmall))
                    Text(
                        text = "Буюртма яратиш",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onCreateExchange()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                    shape = RoundedCornerShape(CompactScreenConstants.CardCornerRadius)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(CompactScreenConstants.SpacingSmall))
                    Text(
                        text = "Етказиб бериш хизмати",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        dismissButton = {
            TextButton(
                modifier = Modifier.padding(top = 20.dp),
                onClick = onDismiss,
                shape = RoundedCornerShape(CompactScreenConstants.CardCornerRadius)
            ) {
                Text(
                    text = "Бекор қилиш",
                    color = primaryColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}
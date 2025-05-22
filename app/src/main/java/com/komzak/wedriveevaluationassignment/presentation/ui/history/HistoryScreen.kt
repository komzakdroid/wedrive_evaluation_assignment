package com.komzak.wedriveevaluationassignment.presentation.ui.history

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.komzak.wedriveevaluationassignment.R
import com.komzak.wedriveevaluationassignment.domain.model.BalanceRecordModel
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryColor
import com.valentinilk.shimmer.shimmer
import org.koin.compose.viewmodel.koinViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object ModernHistoryConstants {
    val ScreenPadding = 20.dp
    val CardPadding = 16.dp
    val CardCornerRadius = 20.dp
    val LargeCardCornerRadius = 24.dp
    val AvatarSize = 64.dp
    val SpacingTiny = 4.dp
    val SpacingSmall = 8.dp
    val SpacingMedium = 16.dp
    val SpacingLarge = 24.dp
    val SpacingXLarge = 32.dp
}

// Modern gradient backgrounds
private val modernHistoryBackground = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF667eea),
        Color(0xFF764ba2),
        Color(0xFFF093fb)
    )
)

private val glassmorphismCard = Brush.linearGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.25f),
        Color.White.copy(alpha = 0.1f)
    )
)

private val whiteGlassCard = Brush.linearGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.95f),
        Color.White.copy(alpha = 0.85f)
    )
)

data class HistoryFilter(
    val label: String,
    val isSelected: Boolean,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ModernHistoryContent(
        uiState = uiState,
        onRefresh = { viewModel.refresh() }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun ModernHistoryContent(
    uiState: HistoryUiState,
    onRefresh: () -> Unit
) {
    var isRefreshing by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("All") }

    LaunchedEffect(uiState.isLoading) {
        isRefreshing = uiState.isLoading
    }

    val filteredRecords = remember(uiState.records, selectedFilter) {
        when (selectedFilter) {
            "Income" -> uiState.records.filter { it.type == 1 }
            "Expense" -> uiState.records.filter { it.type != 1 }
            else -> uiState.records
        }
    }

    val filters = listOf(
        HistoryFilter("All", selectedFilter == "All", Icons.Default.List) {
            selectedFilter = "All"
        },
        HistoryFilter("Income", selectedFilter == "Income", Icons.Default.KeyboardArrowUp) {
            selectedFilter = "Income"
        },
        HistoryFilter("Expense", selectedFilter == "Expense", Icons.Default.KeyboardArrowDown) {
            selectedFilter = "Expense"
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(modernHistoryBackground)
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
                    start = ModernHistoryConstants.ScreenPadding,
                    end = ModernHistoryConstants.ScreenPadding,
                    top = ModernHistoryConstants.SpacingXLarge,
                    bottom = ModernHistoryConstants.SpacingXLarge
                ),
                verticalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingLarge)
            ) {
                item {
                    ModernHistoryHeader(phone = uiState.phone)
                }

                item {
                    HistoryStatsCard(records = uiState.records)
                }

                item {
                    ModernFilterSection(
                        filters = filters,
                        selectedFilter = selectedFilter
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Transaction History",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.2f)
                            )
                        ) {
                            Text(
                                text = "${filteredRecords.size} items",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                modifier = Modifier.padding(
                                    horizontal = ModernHistoryConstants.SpacingSmall,
                                    vertical = ModernHistoryConstants.SpacingTiny
                                )
                            )
                        }
                    }
                }

                if (uiState.isLoading) {
                    items(5) {
                        ModernHistoryItemShimmer()
                    }
                } else {
                    items(filteredRecords) { record ->
                        ModernHistoryItem(
                            record = record,
                            modifier = Modifier.animateItemPlacement()
                        )
                    }

                    if (filteredRecords.isEmpty() && uiState.errorMessage == null) {
                        item {
                            EmptyHistoryCard(filterType = selectedFilter)
                        }
                    }
                }

                if (uiState.errorMessage != null) {
                    item {
                        ErrorCard(errorMessage = uiState.errorMessage)
                    }
                }
            }
        }
    }
}

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
private fun HistoryStatsCard(records: List<BalanceRecordModel>) {
    val totalRecords = records.size
    val incomeRecords = records.count { it.type == 1 }
    val expenseRecords = records.count { it.type != 1 }
    val totalAmount = records.fold(0.0) { acc, record ->
        acc + (record.amount?.toDouble() ?: 0.0)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ModernHistoryConstants.LargeCardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteGlassCard)
                .padding(ModernHistoryConstants.SpacingLarge)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingMedium)
            ) {
                Text(
                    text = "Account Overview",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatsItem(
                        title = "Total",
                        value = totalRecords.toString(),
                        icon = Icons.Default.Settings,
                        color = Color(0xFF2196F3)
                    )
                    StatsItem(
                        title = "Income",
                        value = incomeRecords.toString(),
                        icon = Icons.Default.KeyboardArrowUp,
                        color = Color(0xFF4CAF50)
                    )
                    StatsItem(
                        title = "Expense",
                        value = expenseRecords.toString(),
                        icon = Icons.Default.KeyboardArrowDown,
                        color = Color(0xFFF44336)
                    )
                    StatsItem(
                        title = "Amount",
                        value = String.format("%.1fK", totalAmount / 1000),
                        icon = Icons.Default.Settings,
                        color = Color(0xFF9C27B0)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsItem(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingSmall)
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
private fun ModernFilterSection(
    filters: List<HistoryFilter>,
    selectedFilter: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ModernHistoryConstants.LargeCardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteGlassCard)
                .padding(ModernHistoryConstants.CardPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingMedium)
            ) {
                Text(
                    text = "Filter Transactions",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingMedium),
                    contentPadding = PaddingValues(horizontal = ModernHistoryConstants.SpacingSmall)
                ) {
                    items(filters) { filter ->
                        FilterChipButton(
                            label = filter.label,
                            icon = filter.icon,
                            isSelected = filter.isSelected,
                            onClick = filter.onClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterChipButton(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) primaryColor else Color.White.copy(alpha = 0.8f)
    val contentColor = if (isSelected) Color.White else primaryColor
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Card(
        modifier = Modifier
            .scale(scale),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 6.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = ModernHistoryConstants.SpacingMedium,
                vertical = ModernHistoryConstants.SpacingSmall
            ),
            horizontalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingTiny),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = contentColor
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ModernHistoryItem(
    record: BalanceRecordModel,
    modifier: Modifier = Modifier
) {
    val isIncome = record.type == 1
    val amountColor = if (isIncome) Color(0xFF4CAF50) else Color(0xFFF44336)
    val amountPrefix = if (isIncome) "+" else "−"
    val iconResource = if (isIncome) R.drawable.ic_incoming else R.drawable.ic_outgoing

    val formattedDate = record.createdAt?.let {
        try {
            val zonedDateTime = ZonedDateTime.parse(it)
            zonedDateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        } catch (e: Exception) {
            it
        }
    } ?: ""

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ModernHistoryConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteGlassCard)
                .padding(ModernHistoryConstants.CardPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingMedium)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingMedium)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(amountColor.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(iconResource),
                                contentDescription = if (isIncome) "Income" else "Expense",
                                modifier = Modifier.size(24.dp),
                                tint = amountColor
                            )
                        }
                        Column {
                            Text(
                                text = "$amountPrefix${record.amount ?: 0.0} ${record.currencyType ?: ""}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = amountColor
                            )
                            Text(
                                text = if (isIncome) "Income" else "Expense",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Black.copy(alpha = 0.6f)
                            )
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = formattedDate,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black.copy(alpha = 0.6f)
                        )
                        if (record.serialNo != null) {
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = primaryColor.copy(alpha = 0.1f)
                                )
                            ) {
                                Text(
                                    text = "S/N: ${record.serialNo}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = primaryColor,
                                    modifier = Modifier.padding(
                                        horizontal = ModernHistoryConstants.SpacingSmall,
                                        vertical = ModernHistoryConstants.SpacingTiny
                                    )
                                )
                            }
                        }
                    }
                }

                if (!record.details.isNullOrEmpty() && record.details != "No comment") {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Black.copy(alpha = 0.05f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(ModernHistoryConstants.SpacingMedium),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingSmall)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Details",
                                modifier = Modifier.size(16.dp),
                                tint = Color.Black.copy(alpha = 0.6f)
                            )
                            Text(
                                text = record.details,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black.copy(alpha = 0.8f),
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
private fun ModernHistoryItemShimmer() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ModernHistoryConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteGlassCard)
                .padding(ModernHistoryConstants.CardPadding)
                .shimmer()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingSmall)
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(if (index == 2) 0.6f else 0.8f)
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
private fun EmptyHistoryCard(filterType: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ModernHistoryConstants.LargeCardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteGlassCard)
                .padding(ModernHistoryConstants.SpacingXLarge),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingMedium)
            ) {
                Icon(
                    imageVector = when (filterType) {
                        "Income" -> Icons.Outlined.KeyboardArrowUp
                        "Expense" -> Icons.Outlined.KeyboardArrowDown
                        else -> Icons.Outlined.Settings
                    },
                    contentDescription = "No records",
                    modifier = Modifier.size(64.dp),
                    tint = Color.Black.copy(alpha = 0.3f)
                )
                Text(
                    text = when (filterType) {
                        "Income" -> "No Income Records"
                        "Expense" -> "No Expense Records"
                        else -> "No Records Found"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black.copy(alpha = 0.6f)
                )
                Text(
                    text = when (filterType) {
                        "Income" -> "You haven't received any payments yet"
                        "Expense" -> "You haven't made any expenses yet"
                        else -> "Start making transactions to see your history"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black.copy(alpha = 0.4f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ErrorCard(errorMessage: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ModernHistoryConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ModernHistoryConstants.CardPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingSmall)
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
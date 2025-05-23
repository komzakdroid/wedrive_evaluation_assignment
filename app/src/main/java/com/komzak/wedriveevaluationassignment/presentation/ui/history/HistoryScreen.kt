package com.komzak.wedriveevaluationassignment.presentation.ui.history

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
    val ScreenPadding = 16.dp // Reduced from 20.dp
    val CardPadding = 12.dp // Reduced from 16.dp
    val CardCornerRadius = 16.dp // Reduced from 20.dp
    val LargeCardCornerRadius = 20.dp // Reduced from 24.dp
    val AvatarSize = 48.dp // Reduced from 64.dp
    val SpacingTiny = 2.dp // Reduced from 4.dp
    val SpacingSmall = 6.dp // Reduced from 8.dp
    val SpacingMedium = 12.dp // Reduced from 16.dp
    val SpacingLarge = 18.dp // Reduced from 24.dp
    val SpacingXLarge = 24.dp // Reduced from 32.dp
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
    var selectedFilter by remember { mutableStateOf("Ҳаммаси") }

    LaunchedEffect(uiState.isLoading) {
        isRefreshing = uiState.isLoading
    }

    val filteredRecords = remember(uiState.records, selectedFilter) {
        when (selectedFilter) {
            "Кирим" -> uiState.records.filter { it.type == 1 }
            "Чиқим" -> uiState.records.filter { it.type != 1 }
            else -> uiState.records
        }
    }

    val filters = listOf(
        HistoryFilter("Ҳаммаси", selectedFilter == "Ҳаммаси", Icons.Default.List) {
            selectedFilter = "Ҳаммаси"
        },
        HistoryFilter("Кирим", selectedFilter == "Кирим", Icons.Default.KeyboardArrowUp) {
            selectedFilter = "Кирим"
        },
        HistoryFilter("Чиқим", selectedFilter == "Чиқим", Icons.Default.KeyboardArrowDown) {
            selectedFilter = "Чиқим"
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
                verticalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingMedium) // Reduced spacing
            ) {
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
                            text = "Транзакциялар Тарихи",
                            style = MaterialTheme.typography.titleMedium, // Smaller typography
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Card(
                            shape = RoundedCornerShape(10.dp), // Slightly smaller corner radius
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.2f)
                            )
                        ) {
                            Text(
                                text = "${filteredRecords.size} элементлар",
                                style = MaterialTheme.typography.labelSmall, // Smaller text
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
            .padding(vertical = ModernHistoryConstants.SpacingSmall), // Reduced padding
        shape = RoundedCornerShape(ModernHistoryConstants.LargeCardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(glassmorphismCard)
                .padding(ModernHistoryConstants.SpacingMedium) // Reduced padding
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ҲИСОБЧИ",
                        style = MaterialTheme.typography.titleLarge, // Smaller typography
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Профил: $phone",
                        style = MaterialTheme.typography.bodySmall, // Smaller text
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(ModernHistoryConstants.AvatarSize) // Smaller avatar
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
                        contentDescription = "Профил",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp) // Smaller icon
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
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) // Reduced elevation
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteGlassCard)
                .padding(ModernHistoryConstants.SpacingMedium) // Reduced padding
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingSmall)
            ) {
                Text(
                    text = "Ҳисоб Обзори",
                    style = MaterialTheme.typography.titleMedium, // Smaller typography
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatsItem(
                        title = "Жами",
                        value = totalRecords.toString(),
                        icon = Icons.Default.Settings,
                        color = Color(0xFF2196F3)
                    )
                    StatsItem(
                        title = "Кирим",
                        value = incomeRecords.toString(),
                        icon = Icons.Default.KeyboardArrowUp,
                        color = Color(0xFF4CAF50)
                    )
                    StatsItem(
                        title = "Чиқим",
                        value = expenseRecords.toString(),
                        icon = Icons.Default.KeyboardArrowDown,
                        color = Color(0xFFF44336)
                    )
                    StatsItem(
                        title = "Сумма",
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
        verticalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingTiny)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp) // Smaller size
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(16.dp) // Smaller icon
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Black.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall, // Smaller typography
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
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp) // Reduced elevation
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteGlassCard)
                .padding(ModernHistoryConstants.CardPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingSmall)
            ) {
                Text(
                    text = "Транзакцияларни Фильтрлаш",
                    style = MaterialTheme.typography.titleSmall, // Smaller typography
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingSmall),
                    contentPadding = PaddingValues(horizontal = ModernHistoryConstants.SpacingTiny)
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
        targetValue = if (isSelected) 1.03f else 1f, // Slightly less scale for compactness
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Card(
        modifier = Modifier
            .padding(4.dp)
            .scale(scale),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp), // Smaller corner radius
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = ModernHistoryConstants.SpacingSmall,
                vertical = ModernHistoryConstants.SpacingMedium
            ),
            horizontalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingTiny),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp), // Smaller icon
                tint = contentColor
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge, // Smaller typography
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
            zonedDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        } catch (e: Exception) {
            it
        }
    } ?: ""

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ModernHistoryConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp) // Reduced elevation
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteGlassCard)
                .padding(ModernHistoryConstants.CardPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingSmall)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingSmall)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp) // Smaller size
                                .clip(CircleShape)
                                .background(amountColor.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(iconResource),
                                contentDescription = if (isIncome) "Кирим" else "Чиқим",
                                modifier = Modifier.size(18.dp), // Smaller icon
                                tint = amountColor
                            )
                        }
                        Column {
                            Text(
                                text = "$amountPrefix${record.amount ?: 0.0} ${record.currencyType ?: ""}",
                                style = MaterialTheme.typography.titleSmall, // Smaller typography
                                fontWeight = FontWeight.Bold,
                                color = amountColor
                            )
                            Text(
                                text = if (isIncome) "Кирим" else "Чиқим",
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
                    /*    if (record.serialNo != null) {
                            Card(
                                shape = RoundedCornerShape(6.dp), // Smaller corner radius
                                colors = CardDefaults.cardColors(
                                    containerColor = primaryColor.copy(alpha = 0.1f)
                                )
                            ) {
                                Text(
                                    text = "С/Н: ${record.serialNo}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = primaryColor,
                                    modifier = Modifier.padding(
                                        horizontal = ModernHistoryConstants.SpacingTiny,
                                        vertical = ModernHistoryConstants.SpacingTiny
                                    )
                                )
                            }
                        }*/
                    }
                }

                if (!record.details.isNullOrEmpty() && record.details != "No comment") {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp), // Smaller corner radius
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Black.copy(alpha = 0.05f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(ModernHistoryConstants.SpacingSmall),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingTiny)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Тафсилотлар",
                                modifier = Modifier.size(14.dp), // Smaller icon
                                tint = Color.Black.copy(alpha = 0.6f)
                            )
                            Text(
                                text = record.details,
                                style = MaterialTheme.typography.bodySmall, // Smaller typography
                                color = Color.Black.copy(alpha = 0.8f),
                                maxLines = 5,
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
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteGlassCard)
                .padding(ModernHistoryConstants.CardPadding)
                .shimmer()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingTiny)
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(if (index == 2) 0.6f else 0.8f)
                            .height(14.dp) // Smaller height
                            .clip(RoundedCornerShape(6.dp))
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
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteGlassCard)
                .padding(ModernHistoryConstants.SpacingLarge),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingSmall)
            ) {
                Icon(
                    imageVector = when (filterType) {
                        "Кирим" -> Icons.Outlined.KeyboardArrowUp
                        "Чиқим" -> Icons.Outlined.KeyboardArrowDown
                        else -> Icons.Outlined.Settings
                    },
                    contentDescription = "Ҳеч Нарса Топилмади",
                    modifier = Modifier.size(48.dp), // Smaller icon
                    tint = Color.Black.copy(alpha = 0.3f)
                )
                Text(
                    text = when (filterType) {
                        "Кирим" -> "Кирим Юзасидан Йўқ"
                        "Чиқим" -> "Чиқим Юзасидан Йўқ"
                        else -> "Ҳеч Нарса Топилмади"
                    },
                    style = MaterialTheme.typography.titleSmall, // Smaller typography
                    fontWeight = FontWeight.Medium,
                    color = Color.Black.copy(alpha = 0.6f)
                )
                Text(
                    text = when (filterType) {
                        "Кирим" -> "Сиз ҳали тўловларни олмадингиз"
                        "Чиқим" -> "Сиз ҳали чиқимлар қилмадингиз"
                        else -> "Тарихингизни кўриш учун транзакцияларни бошланг"
                    },
                    style = MaterialTheme.typography.bodySmall, // Smaller typography
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
            horizontalArrangement = Arrangement.spacedBy(ModernHistoryConstants.SpacingTiny)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Хато",
                tint = Color(0xFFFF9800),
                modifier = Modifier.size(20.dp) // Smaller icon
            )
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall, // Smaller typography
                color = Color(0xFFE65100)
            )
        }
    }
}
package com.komzak.wedriveevaluationassignment.presentation.ui.orders

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.komzak.wedriveevaluationassignment.R
import com.komzak.wedriveevaluationassignment.domain.model.TransactionModel
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryColor
import org.koin.compose.viewmodel.koinViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object ModernOrdersConstants {
    val ScreenPadding = 12.dp
    val CardPadding = 10.dp
    val CardCornerRadius = 12.dp
    val LargeCardCornerRadius = 16.dp
    val FilterChipCornerRadius = 10.dp
    val SpacingTiny = 2.dp
    val SpacingSmall = 4.dp
    val SpacingMedium = 8.dp
    val SpacingLarge = 12.dp
    val SpacingXLarge = 16.dp
}

private val modernOrdersBackground = Brush.verticalGradient(
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

data class FilterChip(
    val label: String,
    val isSelected: Boolean,
    val icon: ImageVector? = null,
    val onClick: () -> Unit
)

@Composable
fun OrdersHistoryScreen(
    viewModel: OrdersHistoryViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ModernOrdersHistoryContent(
        uiState = uiState,
        onRefresh = { viewModel.refresh() },
        onUserIdSelected = { viewModel.updateUserId(it) },
        onReceiverIdSelected = { viewModel.updateReceiverId(it) },
        onBalanceIdSelected = { viewModel.updateBalanceId(it) },
        onStatusSelected = { viewModel.updateStatus(it) },
        onDateRangeSelected = { from, to -> viewModel.updateDateRange(from, to) },
        onClearFilters = { viewModel.clearFilters() },
        onCompleteTransaction = { serialNo -> viewModel.completeTransaction(serialNo) }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun ModernOrdersHistoryContent(
    uiState: OrdersHistoryUiState,
    onRefresh: () -> Unit,
    onUserIdSelected: (Int?) -> Unit,
    onReceiverIdSelected: (Int?) -> Unit,
    onBalanceIdSelected: (Int?) -> Unit,
    onStatusSelected: (Long?) -> Unit,
    onDateRangeSelected: (String, String) -> Unit,
    onClearFilters: () -> Unit,
    onCompleteTransaction: (String) -> Unit
) {
    var isRefreshing by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var isSelectingFromDate by remember { mutableStateOf(true) }
    var showFiltersBottomSheet by remember { mutableStateOf(false) }
    var isYuborilganSelected by remember { mutableStateOf(false) }
    var isQabulQilinganSelected by remember { mutableStateOf(false) }
    var showCompleteDialog by remember { mutableStateOf(false) }
    var selectedTransactionSerialNo by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uiState.isLoading) {
        isRefreshing = uiState.isLoading
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(modernOrdersBackground)
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
                    start = ModernOrdersConstants.ScreenPadding,
                    end = ModernOrdersConstants.ScreenPadding,
                    top = ModernOrdersConstants.SpacingLarge,
                    bottom = ModernOrdersConstants.SpacingXLarge
                ),
                verticalArrangement = Arrangement.spacedBy(ModernOrdersConstants.SpacingMedium)
            ) {
                item {
                    OrdersStatsCard(transactions = uiState.transactions)
                }

                item {
                    ModernFilterSection(
                        uiState = uiState,
                        isYuborilganSelected = isYuborilganSelected,
                        isQabulQilinganSelected = isQabulQilinganSelected,
                        onYuborilganClick = {
                            isYuborilganSelected = !isYuborilganSelected
                            isQabulQilinganSelected = false
                            if (isYuborilganSelected) {
                                onUserIdSelected(uiState.availableUserIds.firstOrNull())
                            } else {
                                onClearFilters()
                            }
                        },
                        onQabulQilinganClick = {
                            isQabulQilinganSelected = !isQabulQilinganSelected
                            isYuborilganSelected = false
                            if (isQabulQilinganSelected) {
                                onReceiverIdSelected(uiState.availableUserIds.firstOrNull())
                            } else {
                                onClearFilters()
                            }
                        },
                        onFiltersClick = { showFiltersBottomSheet = true },
                        onClearFilters = {
                            isYuborilganSelected = false
                            isQabulQilinganSelected = false
                            onClearFilters()
                        }
                    )
                }

                item {
                    Text(
                        text = "Транзакциялар тарихи",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = ModernOrdersConstants.SpacingSmall)
                    )
                }

                if (uiState.isLoading) {
                    items(5) {
                        ModernTransactionCardShimmer()
                    }
                } else {
                    items(uiState.transactions) { transaction ->
                        ModernTransactionCard(
                            transaction = transaction,
                            modifier = Modifier.animateItemPlacement(),
                            onClick = {
                                if (transaction.status == 1L) {
                                    selectedTransactionSerialNo = transaction.serialNo
                                    showCompleteDialog = true
                                }
                            }
                        )
                    }

                    if (uiState.transactions.isEmpty() && uiState.errorMessage == null) {
                        item {
                            EmptyStateCard()
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

        AnimatedVisibility(
            visible = !isRefreshing,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(ModernOrdersConstants.ScreenPadding)
        ) {
            FloatingActionButton(
                onClick = { showFiltersBottomSheet = true },
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Фильтр",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        if (showFiltersBottomSheet) {
            ModernFiltersBottomSheet(
                uiState = uiState,
                onDismiss = { showFiltersBottomSheet = false },
                onUserIdSelected = onUserIdSelected,
                onBalanceIdSelected = onBalanceIdSelected,
                onStatusSelected = onStatusSelected,
                onDateRangeSelected = onDateRangeSelected,
                onClearFilters = {
                    isYuborilganSelected = false
                    isQabulQilinganSelected = false
                    onClearFilters()
                },
                onDatePickerClick = { showDatePicker = true }
            )
        }

        if (showDatePicker) {
            ModernDateRangePickerDialog(
                onDismiss = { showDatePicker = false },
                onDateSelected = { from, to ->
                    onDateRangeSelected(from, to)
                    showDatePicker = false
                },
                isSelectingFrom = isSelectingFromDate,
                setIsSelectingFrom = { isSelectingFromDate = it }
            )
        }

        if (showCompleteDialog && selectedTransactionSerialNo != null) {
            CompleteTransactionDialog(
                onDismiss = { showCompleteDialog = false },
                onConfirm = {
                    onCompleteTransaction(selectedTransactionSerialNo!!)
                    showCompleteDialog = false
                    selectedTransactionSerialNo = null
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompleteTransactionDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Транзакцияни тугатиш",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )
        },
        text = {
            Text(
                text = "Ушбу транзакцияни тугатмоқчимисиз?",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black.copy(alpha = 0.8f)
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = primaryColor)
            ) {
                Text(
                    text = "Тасдиқлаш",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Gray)
            ) {
                Text(
                    text = "Бекор қилиш",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(ModernOrdersConstants.LargeCardCornerRadius)
    )
}

@Composable
private fun ModernFilterSection(
    uiState: OrdersHistoryUiState,
    isYuborilganSelected: Boolean,
    isQabulQilinganSelected: Boolean,
    onYuborilganClick: () -> Unit,
    onQabulQilinganClick: () -> Unit,
    onFiltersClick: () -> Unit,
    onClearFilters: () -> Unit
) {
    val hasActiveFilters = uiState.selectedUserId != null ||
            uiState.selectedReceiverId != null ||
            uiState.selectedBalanceId != null ||
            uiState.selectedStatus != null ||
            uiState.selectedDateRange != null ||
            isYuborilganSelected || isQabulQilinganSelected

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ModernOrdersConstants.LargeCardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteGlassCard)
                .padding(ModernOrdersConstants.CardPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(ModernOrdersConstants.SpacingSmall)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Фильтрлар",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )

                    if (hasActiveFilters) {
                        TextButton(onClick = onClearFilters) {
                            Text(
                                "Барчасини тозалаш",
                                color = Color(0xFFF44336),
                                fontWeight = FontWeight.Medium,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(ModernOrdersConstants.SpacingSmall),
                    contentPadding = PaddingValues(horizontal = ModernOrdersConstants.SpacingTiny)
                ) {
                    item {
                        FilterChipButton(
                            label = "Юборилган",
                            icon = Icons.Default.Settings,
                            isSelected = isYuborilganSelected,
                            onClick = onYuborilganClick
                        )
                    }

                    item {
                        FilterChipButton(
                            label = "Қабул қилинган",
                            icon = Icons.Default.Settings,
                            isSelected = isQabulQilinganSelected,
                            onClick = onQabulQilinganClick
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
    val backgroundColor =
        if (isSelected) primaryColor else Color(0xFFE0E0E0)
    val contentColor =
        if (isSelected) Color.White else Color.DarkGray

    Card(
        modifier = Modifier
            .padding(2.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(ModernOrdersConstants.FilterChipCornerRadius),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = ModernOrdersConstants.SpacingSmall,
                vertical = ModernOrdersConstants.SpacingTiny
            ),
            horizontalArrangement = Arrangement.spacedBy(ModernOrdersConstants.SpacingTiny),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = contentColor
            )
            Text(
                modifier = Modifier.padding(ModernOrdersConstants.SpacingMedium),
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun OrdersStatsCard(transactions: List<TransactionModel>) {
    val totalTransactions = transactions.size
    val completedTransactions = transactions.count { it.status == 2L }
    val pendingTransactions = transactions.count { it.status == 1L }
    val totalAmount = transactions.fold(0.0) { acc, transaction ->
        acc + transaction.amount.toDouble()
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ModernOrdersConstants.LargeCardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteGlassCard)
                .padding(ModernOrdersConstants.SpacingMedium)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(ModernOrdersConstants.SpacingSmall)
            ) {
                Text(
                    text = "Статистика кўриниши",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatsItem(
                        title = "Жами",
                        value = totalTransactions.toString(),
                        icon = Icons.Default.Settings,
                        color = Color(0xFF2196F3)
                    )
                    StatsItem(
                        title = "Тугалланган",
                        value = completedTransactions.toString(),
                        icon = Icons.Default.CheckCircle,
                        color = Color(0xFF4CAF50)
                    )
                    StatsItem(
                        title = "Кутилмоқда",
                        value = pendingTransactions.toString(),
                        icon = Icons.Default.Settings,
                        color = Color(0xFFFF9800)
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
        verticalArrangement = Arrangement.spacedBy(ModernOrdersConstants.SpacingTiny)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Black.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
private fun ModernTransactionCard(
    transaction: TransactionModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isIncome = transaction.type == 1L
    val amountColor = if (isIncome) Color(0xFF4CAF50) else Color(0xFFF44336)
    val amountPrefix = if (isIncome) "+" else "−"
    val iconResource = if (isIncome) R.drawable.ic_incoming else R.drawable.ic_outgoing
    val context = LocalContext.current
    val formattedDate = transaction.createdAt.let {
        try {
            val zonedDateTime = ZonedDateTime.parse(it)
            zonedDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        } catch (e: Exception) {
            it
        }
    } ?: ""

    val statusText = when (transaction.status) {
        1L -> "Кутилмоқда"
        2L -> "Тугалланган"
        else -> "Номаълум"
    }

    val statusColor = when (transaction.status) {
        1L -> Color(0xFFFF9800)
        2L -> Color(0xFF4CAF50)
        else -> Color.Gray
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(ModernOrdersConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteGlassCard)
                .padding(ModernOrdersConstants.CardPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(ModernOrdersConstants.SpacingSmall)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(ModernOrdersConstants.SpacingTiny)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(amountColor.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(iconResource),
                                contentDescription = if (isIncome) "Кирим" else "Чиқим",
                                modifier = Modifier.size(18.dp),
                                tint = amountColor
                            )
                        }
                        Column {
                            Text(
                                text = "$amountPrefix${transaction.amount} ${transaction.fromCurrencyType}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = amountColor
                            )
                            Text(
                                text = formattedDate,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Black.copy(alpha = 0.6f)
                            )
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = statusColor.copy(alpha = 0.1f))
                    ) {
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.labelSmall,
                            color = statusColor,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(
                                horizontal = ModernOrdersConstants.SpacingTiny,
                                vertical = ModernOrdersConstants.SpacingTiny
                            )
                        )
                    }
                }

                if (transaction.details.isNotEmpty() == true) {
                    Text(
                        modifier = Modifier.padding(vertical = 4.dp),
                        text = transaction.details,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black.copy(alpha = 0.8f),
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(ModernOrdersConstants.SpacingTiny)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Қабул қилувчи",
                            modifier = Modifier.size(14.dp),
                            tint = primaryColor
                        )
                        Text(
                            text = transaction.receiverName,
                            style = MaterialTheme.typography.bodySmall,
                            color = primaryColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Box(
                        modifier = Modifier.background(
                            color = Color.Green.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp)
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(ModernOrdersConstants.SpacingTiny),
                            modifier = Modifier
                                .padding(6.dp)
                                .clickable {
                                    val phoneNumber = transaction.receiverPhone
                                    val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                                        data = "tel:$phoneNumber".toUri()
                                    }
                                    context.startActivity(dialIntent)
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Телефон",
                                modifier = Modifier.size(14.dp),
                                tint = Color.Black.copy(alpha = 0.6f)
                            )
                            Text(
                                text = transaction.receiverPhone,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Black.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernTransactionCardShimmer() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ModernOrdersConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteGlassCard)
                .padding(ModernOrdersConstants.CardPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(ModernOrdersConstants.SpacingTiny)
            ) {
                repeat(4) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(if (it == 3) 0.6f else 0.8f)
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.Black.copy(alpha = 0.1f))
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyStateCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ModernOrdersConstants.LargeCardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteGlassCard)
                .padding(ModernOrdersConstants.SpacingLarge),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(ModernOrdersConstants.SpacingSmall)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Транзакциялар йўқ",
                    modifier = Modifier.size(48.dp),
                    tint = Color.Black.copy(alpha = 0.3f)
                )
                Text(
                    text = "Транзакциялар топилмади",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black.copy(alpha = 0.6f)
                )
                Text(
                    text = "Фильтрларни сўндиринг ёки кейинроқ қайта кўринг",
                    style = MaterialTheme.typography.bodySmall,
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
        shape = RoundedCornerShape(ModernOrdersConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ModernOrdersConstants.CardPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ModernOrdersConstants.SpacingTiny)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Хато",
                tint = Color(0xFFFF9800),
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFE65100)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModernFiltersBottomSheet(
    uiState: OrdersHistoryUiState,
    onDismiss: () -> Unit,
    onUserIdSelected: (Int?) -> Unit,
    onBalanceIdSelected: (Int?) -> Unit,
    onStatusSelected: (Long?) -> Unit,
    onDateRangeSelected: (String, String) -> Unit,
    onClearFilters: () -> Unit,
    onDatePickerClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(
            topStart = ModernOrdersConstants.LargeCardCornerRadius,
            topEnd = ModernOrdersConstants.LargeCardCornerRadius
        ),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ModernOrdersConstants.SpacingLarge),
            verticalArrangement = Arrangement.spacedBy(ModernOrdersConstants.SpacingMedium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Фильтр танловлари",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )

                TextButton(onClick = onClearFilters) {
                    Text(
                        "Барчасини тозалаш",
                        color = Color(0xFFF44336),
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            ModernFilterDropdown(
                label = "Фойдаланувчи ID сини танланг",
                selected = uiState.selectedUserId?.toString(),
                items = uiState.availableUserIds.map { it.toString() },
                onItemSelected = { onUserIdSelected(it?.toIntOrNull()) },
                icon = Icons.Default.Person
            )
            Spacer(modifier = Modifier.height(ModernOrdersConstants.SpacingSmall))
            ModernFilterDropdown(
                label = "Баланс ID сини танланг",
                selected = uiState.selectedBalanceId?.toString(),
                items = uiState.availableBalanceIds.map { it.toString() },
                onItemSelected = { onBalanceIdSelected(it?.toIntOrNull()) },
                icon = Icons.Default.Settings
            )
            Spacer(modifier = Modifier.height(ModernOrdersConstants.SpacingSmall))

            ModernFilterDropdown(
                label = "Статусни танланг",
                selected = when (uiState.selectedStatus) {
                    1L -> "Қабул қилинган"
                    2L -> "Тугалланган"
                    else -> null
                },
                items = listOf("Кутилмоқда", "Тугалланган"),
                onItemSelected = { selected ->
                    val statusCode = when (selected) {
                        "Кутилмоқда" -> 1L
                        "Тугалланган" -> 2L
                        else -> null
                    }
                    onStatusSelected(statusCode)
                },
                icon = Icons.Default.CheckCircle
            )
            Spacer(modifier = Modifier.height(ModernOrdersConstants.SpacingSmall))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(ModernOrdersConstants.CardCornerRadius),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                border = BorderStroke(0.5.dp, Color.Black.copy(alpha = 0.1f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(ModernOrdersConstants.CardPadding),
                    verticalArrangement = Arrangement.spacedBy(ModernOrdersConstants.SpacingTiny)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(ModernOrdersConstants.SpacingTiny)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Сана диапазони",
                            modifier = Modifier.size(16.dp),
                            tint = primaryColor
                        )
                        Text(
                            text = "Сана диапазони",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium,
                            color = primaryColor
                        )
                    }

                    Text(
                        text = uiState.selectedDateRange?.let { "${it.first} - ${it.second}" }
                            ?: "Сана диапазони танланмаган",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (uiState.selectedDateRange == null) Color.Gray else Color.Black
                    )
                    Spacer(modifier = Modifier.height(ModernOrdersConstants.SpacingSmall))

                    Button(
                        onClick = onDatePickerClick,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        shape = RoundedCornerShape(ModernOrdersConstants.CardCornerRadius)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(ModernOrdersConstants.SpacingTiny))
                        Text(
                            "Сана диапазонини танлаш",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(ModernOrdersConstants.SpacingMedium))
        }
    }
}

@Composable
private fun ModernFilterDropdown(
    label: String,
    selected: String?,
    items: List<String>,
    onItemSelected: (String?) -> Unit,
    icon: ImageVector
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ModernOrdersConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        border = BorderStroke(0.5.dp, Color.Black.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(ModernOrdersConstants.CardPadding),
            verticalArrangement = Arrangement.spacedBy(ModernOrdersConstants.SpacingTiny)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ModernOrdersConstants.SpacingTiny)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(16.dp),
                    tint = primaryColor
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = primaryColor
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Кенгайтириш",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Black.copy(alpha = 0.6f)
                )
            }

            Text(
                text = selected ?: "Ҳеч қайсиси танланмаган",
                style = MaterialTheme.typography.bodySmall,
                color = if (selected == null) Color.Gray else Color.Black
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .clip(RoundedCornerShape(ModernOrdersConstants.CardCornerRadius))
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        "Ҳеч қайсиси",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                },
                onClick = {
                    onItemSelected(null)
                    expanded = false
                }
            )
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            item,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black
                        )
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModernDateRangePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (String, String) -> Unit,
    isSelectingFrom: Boolean,
    setIsSelectingFrom: (Boolean) -> Unit
) {
    var fromDate by remember { mutableStateOf<Long?>(null) }
    var toDate by remember { mutableStateOf<Long?>(null) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = if (isSelectingFrom) fromDate else toDate
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedDate = datePickerState.selectedDateMillis
                    if (isSelectingFrom) {
                        fromDate = selectedDate
                        setIsSelectingFrom(false)
                    } else {
                        toDate = selectedDate
                        if (fromDate != null && toDate != null) {
                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            val from = Instant.ofEpochMilli(fromDate!!)
                                .atZone(ZoneId.systemDefault())
                                .format(formatter)
                            val to = Instant.ofEpochMilli(toDate!!)
                                .atZone(ZoneId.systemDefault())
                                .format(formatter)
                            onDateSelected(from, to)
                        }
                    }
                },
                colors = ButtonDefaults.textButtonColors(contentColor = primaryColor)
            ) {
                Text(
                    if (isSelectingFrom && fromDate == null) "Бошланғич санани танланг"
                    else if (!isSelectingFrom && toDate == null) "Тугатувчи санани танланг"
                    else "Тасдиқлаш",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Gray)
            ) {
                Text("Бекор қилиш", style = MaterialTheme.typography.labelMedium)
            }
        },
        shape = RoundedCornerShape(ModernOrdersConstants.LargeCardCornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(ModernOrdersConstants.CardPadding),
            verticalArrangement = Arrangement.spacedBy(ModernOrdersConstants.SpacingSmall)
        ) {
            Card(
                shape = RoundedCornerShape(ModernOrdersConstants.CardCornerRadius),
                colors = CardDefaults.cardColors(containerColor = primaryColor.copy(alpha = 0.1f))
            ) {
                Text(
                    text = if (isSelectingFrom) "Бошланғич санани танланг" else "Тугатувчи санани танланг",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor,
                    modifier = Modifier.padding(ModernOrdersConstants.SpacingSmall)
                )
            }

            if (fromDate != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Дан: ${
                            Instant.ofEpochMilli(fromDate!!).atZone(ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                        }",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black.copy(alpha = 0.8f)
                    )
                    if (!isSelectingFrom && toDate != null) {
                        Text(
                            text = "Га: ${
                                Instant.ofEpochMilli(toDate!!).atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                            }",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            DatePicker(state = datePickerState)
        }
    }
}
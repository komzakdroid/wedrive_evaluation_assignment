package com.komzak.wedriveevaluationassignment.presentation.ui.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.komzak.wedriveevaluationassignment.R
import com.komzak.wedriveevaluationassignment.domain.model.TransactionModel
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryColor
import com.komzak.wedriveevaluationassignment.presentation.theme.secondaryBackground
import com.komzak.wedriveevaluationassignment.presentation.theme.whiteColor
import com.komzak.wedriveevaluationassignment.presentation.ui.home.ScreenConstants
import org.koin.compose.viewmodel.koinViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun OrdersHistoryScreen(
    viewModel: OrdersHistoryViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    OrdersHistoryContent(
        uiState = uiState,
        onRefresh = { viewModel.refresh() },
        onUserIdSelected = { viewModel.updateUserId(it) },
        onBalanceIdSelected = { viewModel.updateBalanceId(it) },
        onStatusSelected = { viewModel.updateStatus(it) },
        onDateRangeSelected = { from, to -> viewModel.updateDateRange(from, to) },
        onClearFilters = { viewModel.clearFilters() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrdersHistoryContent(
    uiState: OrdersHistoryUiState,
    onRefresh: () -> Unit,
    onUserIdSelected: (Int?) -> Unit,
    onBalanceIdSelected: (Int?) -> Unit,
    onStatusSelected: (Long?) -> Unit,
    onDateRangeSelected: (String, String) -> Unit,
    onClearFilters: () -> Unit
) {
    var isRefreshing by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var isSelectingFromDate by remember { mutableStateOf(true) }

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
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        secondaryBackground,
                        secondaryBackground.copy(alpha = 0.8f)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(ScreenConstants.ScreenPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OrdersHistoryHeader(uiState.phone)

                OrdersHistoryFilters(
                    userId = uiState.selectedUserId,
                    balanceId = uiState.selectedBalanceId,
                    status = uiState.selectedStatus,
                    dateRange = uiState.selectedDateRange,
                    availableUserIds = uiState.availableUserIds,
                    availableBalanceIds = uiState.availableBalanceIds,
                    onUserIdSelected = onUserIdSelected,
                    onBalanceIdSelected = onBalanceIdSelected,
                    onStatusSelected = onStatusSelected,
                    onDatePickerClick = { showDatePicker = true },
                    onClearFilters = onClearFilters
                )

                OrdersHistoryList(
                    isLoading = uiState.isLoading,
                    transactions = uiState.transactions,
                    errorMessage = uiState.errorMessage
                )
            }

            if (showDatePicker) {
                DateRangePickerDialog(
                    onDismiss = { showDatePicker = false },
                    onDateSelected = { from, to ->
                        onDateRangeSelected(from, to)
                        showDatePicker = false
                    },
                    isSelectingFrom = isSelectingFromDate,
                    setIsSelectingFrom = { isSelectingFromDate = it }
                )
            }
        }
    }
}

@Composable
private fun OrdersHistoryHeader(phone: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_hisobchi),
            contentDescription = "Profile Icon",
            tint = primaryColor,
            modifier = Modifier.size(32.dp)
        )
        Column {
            Text(
                text = "Buyurtmalar tarixi",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )
            Text(
                text = "Profil: $phone",
                style = MaterialTheme.typography.bodyMedium,
                color = primaryColor.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun OrdersHistoryFilters(
    userId: Int?,
    balanceId: Int?,
    status: Long?,
    dateRange: Pair<String, String>?,
    availableUserIds: List<Int>,
    availableBalanceIds: List<Int>,
    onUserIdSelected: (Int?) -> Unit,
    onBalanceIdSelected: (Int?) -> Unit,
    onStatusSelected: (Long?) -> Unit,
    onDatePickerClick: () -> Unit,
    onClearFilters: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(whiteColor, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        FilterDropdown(
            label = "Foydalanuvchi ID sini tanlang",
            selected = userId?.toString() ?: "Foydalanuvchi ID sini tanlang",
            items = availableUserIds.map { it.toString() },
            onItemSelected = { onUserIdSelected(it.toIntOrNull()) }
        )
        FilterDropdown(
            label = "Hisob ID sini tanlang",
            selected = balanceId?.toString() ?: "Hisob ID sini tanlang",
            items = availableBalanceIds.map { it.toString() },
            onItemSelected = { onBalanceIdSelected(it.toIntOrNull()) }
        )
        FilterDropdown(
            label = "Statusni tanlang",
            selected = when (status) {
                1L -> "Qabul qilingan"
                2L -> "Yakunlangan"
                else -> "Statusni tanlang"
            },
            items = listOf("Qabul qilingan", "Yakunlangan"),
            onItemSelected = {
                val statusCode = when (it) {
                    "Qabul qilingan" -> 1L
                    "Yakunlangan" -> 2L
                    else -> null
                }
                onStatusSelected(statusCode)
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = dateRange?.let { "${it.first} - ${it.second}" } ?: "Muddatni tanlang",
                style = MaterialTheme.typography.bodyMedium,
                color = if (dateRange == null) Color.Gray else primaryColor,
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = onDatePickerClick,
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Pick Dates")
            }
        }
        TextButton(
            onClick = onClearFilters,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = "Clear Filters",
                color = primaryColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun FilterDropdown(
    label: String,
    selected: String,
    items: List<String>,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .background(whiteColor)
            .clickable { expanded = true }
            .padding(12.dp)
    ) {
        Text(
            text = if (selected == "Tanlang") label else selected,
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected == "Tanlang") Color.Gray else primaryColor
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(whiteColor)
        ) {
            DropdownMenuItem(
                text = { Text("Tanlang") },
                onClick = {
                    onItemSelected("Tanlang")
                    expanded = false
                }
            )
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
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
private fun DateRangePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (String, String) -> Unit,
    isSelectingFrom: Boolean,
    setIsSelectingFrom: (Boolean) -> Unit
) {
    var fromDate by remember { mutableStateOf<Long?>(null) }
    var toDate by remember { mutableStateOf<Long?>(null) }
    var pickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val pickerState = pickerState
                    val selectedDate = pickerState.selectedDateMillis
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
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = if (isSelectingFrom) "-Dan" else "-Gacha",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            DatePicker(
                state = rememberDatePickerState(
                    initialSelectedDateMillis = if (isSelectingFrom) fromDate else toDate
                )
            )
        }
    }
}

@Composable
private fun OrdersHistoryList(
    isLoading: Boolean,
    transactions: List<TransactionModel>,
    errorMessage: String?
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = primaryColor)
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = ScreenConstants.ScreenPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (transactions.isEmpty() && errorMessage == null) {
                    item {
                        Text(
                            text = "No transactions found",
                            style = MaterialTheme.typography.bodyMedium,
                            color = primaryColor.copy(alpha = 0.6f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    items(transactions) { transaction ->
                        TransactionItem(transaction = transaction)
                    }
                }
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun TransactionItem(transaction: TransactionModel) {
    val isIncome = transaction.type == 1L
    val amountColor = if (isIncome) Color(0xFF4CAF50) else Color(0xFFF44336)
    val amountPrefix = if (isIncome) "+" else "−"
    val iconResource = if (isIncome) R.drawable.ic_incoming else R.drawable.ic_outgoing

    val formattedDate = transaction.createdAt.let {
        try {
            val zonedDateTime = ZonedDateTime.parse(it)
            zonedDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        } catch (e: Exception) {
            it
        }
    }

    val statusText = when (transaction.status) {
        1L -> "Qabul qilingan"
        2L -> "Yakunlangan"
        else -> "Unknown"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = whiteColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(iconResource),
                    contentDescription = if (isIncome) "Income" else "Outgoing",
                    modifier = Modifier.size(20.dp),
                    tint = amountColor
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$amountPrefix${transaction.amount} ${transaction.fromCurrencyType}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = amountColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = primaryColor.copy(alpha = 0.6f)
                    )
                }

                Text(
                    text = transaction.details.ifEmpty { "No details" },
                    style = MaterialTheme.typography.bodySmall,
                    color = primaryColor.copy(alpha = 0.6f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Status: $statusText",
                    style = MaterialTheme.typography.bodySmall,
                    color = primaryColor.copy(alpha = 0.8f)
                )
                Text(
                    text = "Qabul qiluvchi: ${transaction.receiverName} (${transaction.receiverPhone})",
                    style = MaterialTheme.typography.bodySmall,
                    color = primaryColor.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
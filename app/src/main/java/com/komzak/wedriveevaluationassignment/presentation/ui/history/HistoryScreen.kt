package com.komzak.wedriveevaluationassignment.presentation.ui.history

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.komzak.wedriveevaluationassignment.R
import com.komzak.wedriveevaluationassignment.domain.model.BalanceRecordModel
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryColor
import com.komzak.wedriveevaluationassignment.presentation.theme.secondaryBackground
import com.komzak.wedriveevaluationassignment.presentation.theme.whiteColor
import com.komzak.wedriveevaluationassignment.presentation.ui.home.ScreenConstants
import com.valentinilk.shimmer.shimmer
import org.koin.compose.viewmodel.koinViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HistoryContent(
        uiState = uiState,
        onRefresh = { viewModel.refresh() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoryContent(
    uiState: HistoryUiState,
    onRefresh: () -> Unit
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
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            secondaryBackground,
                            secondaryBackground.copy(alpha = 0.8f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(ScreenConstants.ScreenPadding)
            ) {
                HistoryHeader(uiState.phone)

                Spacer(modifier = Modifier.height(ScreenConstants.SpacingMedium))

                HistoryList(
                    isLoading = uiState.isLoading,
                    records = uiState.records
                )

                if (uiState.errorMessage != null) {
                    Text(
                        text = uiState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(ScreenConstants.SpacingMedium)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryHeader(phone: String) {
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
}

@Composable
private fun HistoryList(
    isLoading: Boolean,
    records: List<BalanceRecordModel>
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = ScreenConstants.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(ScreenConstants.CardPadding)
    ) {
        if (isLoading) {
            items(5) {
                HistoryItemShimmer()
            }
        } else {
            items(records) { record ->
                HistoryItem(record = record)
            }
        }
    }
}

@Composable
private fun HistoryItemShimmer() {
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
                style = MaterialTheme.typography.bodySmall,
                color = primaryColor.copy(alpha = 0.3f)
            )
            Text(
                text = " ".repeat(10),
                style = MaterialTheme.typography.bodySmall,
                color = primaryColor.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
private fun HistoryItem(record: BalanceRecordModel) {
    val isIncome = record.type == 1
    val amountColor =
        if (isIncome) Color(0xFF4CAF50) else Color(0xFFF44336)
    val amountPrefix = if (isIncome) "+" else "−"
    val iconResource = if (isIncome) R.drawable.ic_incoming else R.drawable.ic_outgoing

    val formattedDate = record.createdAt?.let {
        try {
            val zonedDateTime = ZonedDateTime.parse(it)
            zonedDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        } catch (e: Exception) {
            it // Fallback to raw string if parsing fails
        }
    } ?: ""

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
                    painter = painterResource(iconResource),
                    contentDescription = if (isIncome) "Income" else "Outgoing",
                    modifier = Modifier.size(16.dp),
                    tint = amountColor
                )
                Text(
                    text = "$amountPrefix${record.amount ?: 0.0} ${record.currencyType ?: ""}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = amountColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = primaryColor.copy(alpha = 0.6f),
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = record.details ?: "No comment",
                style = MaterialTheme.typography.bodySmall,
                color = primaryColor.copy(alpha = 0.6f),
                fontStyle = FontStyle.Italic,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "S/N: ${record.serialNo ?: "N/A"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = primaryColor.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
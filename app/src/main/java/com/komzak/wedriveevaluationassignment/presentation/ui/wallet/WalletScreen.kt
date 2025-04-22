package com.komzak.wedriveevaluationassignment.presentation.ui.wallet

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.komzak.wedriveevaluationassignment.R
import com.komzak.wedriveevaluationassignment.domain.model.PaymentMethod
import com.komzak.wedriveevaluationassignment.presentation.navigation.NavRoute
import com.komzak.wedriveevaluationassignment.presentation.theme.cardBrush
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryBackground
import com.komzak.wedriveevaluationassignment.presentation.theme.secondaryBackground
import com.komzak.wedriveevaluationassignment.presentation.theme.whiteColor
import com.komzak.wedriveevaluationassignment.presentation.ui.components.CustomSwitch
import com.komzak.wedriveevaluationassignment.presentation.ui.components.PhoneNumberBottomSheet
import com.komzak.wedriveevaluationassignment.presentation.ui.components.PromoCodeBottomSheet
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    viewModel: WalletViewModel = koinViewModel(),
    navController: NavController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val promoSheetState = rememberModalBottomSheetState()
    val phoneSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearErrorMessage()
        }
    }

    LaunchedEffect(uiState.message) {
        uiState.message?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }

    if (uiState.isPromoSheetOpen) {
        PromoCodeBottomSheet(
            promoCode = uiState.promoCode,
            onPromoCodeChange = viewModel::onPromoCodeChange,
            onDismiss = { viewModel.closePromoSheet() },
            onSave = {
                coroutineScope.launch {
                    viewModel.submitPromoCode()
                }
            },
            sheetState = promoSheetState,
            isLoading = uiState.isLoading
        )
    }

    if (uiState.isPhoneSheetOpen) {
        PhoneNumberBottomSheet(
            phoneNumber = uiState.phone,
            onPhoneNumberChange = viewModel::onPhoneNumberChange,
            onDismiss = { viewModel.closePhoneSheet() },
            onSave = {
                coroutineScope.launch {
                    viewModel.submitPhoneNumber()
                }
            },
            sheetState = phoneSheetState,
            isLoading = uiState.isLoading
        )
    }

    WalletScreenContent(
        uiState = uiState,
        onIdentificationClick = { viewModel.openPhoneSheet() },
        onAddPromoCodeClick = {
            if (uiState.isIdentificationRequired) {
                Toast.makeText(
                    context,
                    "Please identify with your phone number first",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.openPromoSheet()
            }
        },
        onAddCardClick = {
            if (uiState.isIdentificationRequired) {
                Toast.makeText(
                    context,
                    "Please identify with your phone number first",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                navController.navigate(NavRoute.AddCard.route)
            }
        },
        onTogglePaymentMethod = { method ->
            if (uiState.isIdentificationRequired) {
                Toast.makeText(
                    context,
                    "Please identify with your phone number first",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.togglePaymentMethod(method)
            }
        },
        logout = {
            coroutineScope.launch {
                viewModel.logout()
            }
        }
    )
}

@Composable
private fun WalletScreenContent(
    uiState: WalletUiState,
    onIdentificationClick: () -> Unit,
    onAddPromoCodeClick: () -> Unit,
    onAddCardClick: () -> Unit,
    onTogglePaymentMethod: (PaymentMethod) -> Unit,
    logout: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBackground)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.wallet),
                style = MaterialTheme.typography.titleLarge,
            )
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable {
                        logout()
                    },
                painter = painterResource(id = R.drawable.ic_logout_24),
                contentDescription = null,
            )
        }
        Spacer(Modifier.height(16.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .background(
                    brush = cardBrush,
                    shape = RoundedCornerShape(16.dp)
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.balance),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = uiState.balance,
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                    )
                    Spacer(Modifier.width(8.dp))
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = whiteColor,
                            strokeCap = StrokeCap.Round,
                            strokeWidth = 3.dp
                        )
                    }
                }


                Spacer(Modifier.height(24.dp))
            }
        }

        if (uiState.isIdentificationRequired) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onIdentificationClick() },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    Modifier.padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_info),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = stringResource(R.string.identification_required),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_arrow_up_right),
                        contentDescription = null
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        WalletRow(
            res = R.drawable.ic_percent,
            text = stringResource(R.string.add_promo),
            onClick = onAddPromoCodeClick
        )

        Spacer(Modifier.height(16.dp))

        WalletRow(
            res = R.drawable.ic_cashcop,
            text = PaymentMethod.CASH.name,
            isToggleable = true,
            isEnabled = uiState.selectedMethod == PaymentMethod.CASH,
            onToggle = { onTogglePaymentMethod(PaymentMethod.CASH) },
            onClick = { onTogglePaymentMethod(PaymentMethod.CASH) }
        )

        Spacer(Modifier.height(16.dp))

        WalletRow(
            res = R.drawable.ic_cards,
            text = PaymentMethod.CARD.name + " ${uiState.selectedCard}",
            isToggleable = true,
            isEnabled = uiState.selectedMethod == PaymentMethod.CARD,
            onToggle = { onTogglePaymentMethod(PaymentMethod.CARD) },
            onClick = { onTogglePaymentMethod(PaymentMethod.CARD) }
        )

        Spacer(Modifier.height(16.dp))

        WalletRow(
            res = R.drawable.ic_add_card,
            text = stringResource(R.string.add_card),
            onClick = onAddCardClick
        )
    }
}

@Composable
private fun WalletRow(
    res: Int,
    text: String,
    onClick: (() -> Unit)? = null,
    isToggleable: Boolean = false,
    isEnabled: Boolean = false,
    onToggle: (() -> Unit)? = null,
    onToggleClick: ((PaymentMethod) -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                enabled = onClick != null || onToggleClick != null,
                onClick = {
                    if (isToggleable && onToggleClick != null) {
                        onToggleClick(
                            if (text.startsWith(PaymentMethod.CASH.name)) PaymentMethod.CASH
                            else PaymentMethod.CARD
                        )
                    } else {
                        onClick?.invoke()
                    }
                }
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = secondaryBackground)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = res),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
            if (isToggleable) {
                CustomSwitch(
                    modifier = Modifier.padding(start = 8.dp),
                    checked = isEnabled,
                    onCheckedChange = { onToggle?.invoke() }
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null
                )
            }
        }
    }
}
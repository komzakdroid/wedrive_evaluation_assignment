package com.komzak.wedriveevaluationassignment.presentation.ui.wallet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.komzak.wedriveevaluationassignment.R
import com.komzak.wedriveevaluationassignment.presentation.navigation.NavRoute
import com.komzak.wedriveevaluationassignment.presentation.theme.cardBrush
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryBackground
import com.komzak.wedriveevaluationassignment.presentation.theme.secondaryBackground
import com.komzak.wedriveevaluationassignment.presentation.ui.components.CustomSwitch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WalletScreen(
    viewModel: WalletViewModel = koinViewModel(),
    navController: NavController = rememberNavController()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    WalletScreenContent(
        uiState = uiState,
        onIdentificationClick = { navController.navigate(NavRoute.CreateUser.route) },
        onAddPromoCodeClick = {/* navController.navigate(NavRoute.AddPromoCode.route)*/ },
        onAddCardClick = {/* navController.navigate(NavRoute.AddCard.withId("new")) */ },
        onTogglePaymentMethod = viewModel::togglePaymentMethod
    )
}

@Composable
private fun WalletScreenContent(
    uiState: WalletUiState,
    onIdentificationClick: () -> Unit,
    onAddPromoCodeClick: () -> Unit,
    onAddCardClick: () -> Unit,
    onTogglePaymentMethod: (PaymentMethodType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBackground)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.wallet),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

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
                Text(
                    text = uiState.balance,
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(Modifier.height(24.dp))
            }
        }
        Spacer(Modifier.height(16.dp))

        if (uiState.isIdentificationRequired) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
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
            text = PaymentMethodType.CASH.name,
            isToggleable = true,
            isEnabled = uiState.selectedMethod == PaymentMethodType.CASH,
            onToggle = { onTogglePaymentMethod(PaymentMethodType.CASH) },
            onClick = { onTogglePaymentMethod(PaymentMethodType.CASH) }
        )

        Spacer(Modifier.height(16.dp))

        WalletRow(
            res = R.drawable.ic_cards,
            text = PaymentMethodType.CARD.name + " ${uiState.selectedMethod}",
            isToggleable = true,
            isEnabled = uiState.selectedMethod == PaymentMethodType.CARD,
            onToggle = { onTogglePaymentMethod(PaymentMethodType.CARD) },
            onClick = { onTogglePaymentMethod(PaymentMethodType.CARD) }
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
    onToggleClick: ((PaymentMethodType) -> Unit)? = null
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
                        onToggleClick.invoke(
                            if (text.startsWith(PaymentMethodType.CASH.name)) PaymentMethodType.CASH
                            else PaymentMethodType.CARD
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
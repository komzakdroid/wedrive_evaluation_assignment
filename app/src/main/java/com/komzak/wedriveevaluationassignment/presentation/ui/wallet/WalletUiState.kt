package com.komzak.wedriveevaluationassignment.presentation.ui.wallet

data class WalletUiState(
    val balance: String = "0,000.00",
    val isIdentificationRequired: Boolean = true,
    val selectedMethod: PaymentMethodType = PaymentMethodType.CASH,
    val selectedCard: String = "**** 1234",
)
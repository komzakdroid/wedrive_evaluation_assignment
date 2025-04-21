package com.komzak.wedriveevaluationassignment.presentation.ui.wallet

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class PaymentMethodType(name: String) {
    CASH("Cash"),
    CARD("Card"),
}


class WalletViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(WalletUiState())
    val uiState = _uiState.asStateFlow()

    fun togglePaymentMethod(methodType: PaymentMethodType) {
        _uiState.update { it.copy(selectedMethod = methodType) }
    }
}
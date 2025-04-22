package com.komzak.wedriveevaluationassignment.presentation.ui.wallet

import com.komzak.wedriveevaluationassignment.domain.model.PaymentMethod
import com.komzak.wedriveevaluationassignment.domain.model.UserModel

data class WalletUiState(
    val balance: String = "0,000.00",
    val isIdentificationRequired: Boolean = true,
    val selectedMethod: PaymentMethod = PaymentMethod.CASH,
    val selectedCard: String = "",
    val activeCardId: Long = -1,
    val user: UserModel? = null,
    val isLoading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val isEmptyCards: Boolean = false,

    val phone: String = "",
    val promoCode:String = "",
    val isPromoSheetOpen:Boolean = false,
    val isPhoneSheetOpen:Boolean = false,
)
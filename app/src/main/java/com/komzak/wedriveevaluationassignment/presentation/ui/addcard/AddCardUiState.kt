package com.komzak.wedriveevaluationassignment.presentation.ui.addcard

data class AddCardUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val phone: String = "",
    val cardNumber: String = "",
    val expiryDate: String = "",
) {
    val isFormFilled: Boolean
        get() = cardNumber.filter { it.isDigit() }.length == 16 && expiryDate.filter { it.isDigit() }.length == 4
}
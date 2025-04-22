package com.komzak.wedriveevaluationassignment.presentation.ui.addcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.local.DataStoreHelper
import com.komzak.wedriveevaluationassignment.domain.usecase.AddCardUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddCardViewModel(
    private val addCardUseCase: AddCardUseCase,
    private val dataStoreHelper: DataStoreHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddCardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getPhoneFromLocale()
    }

    private fun getPhoneFromLocale() {
        viewModelScope.launch {
            dataStoreHelper.getPhoneNumber().collect { savedPhone ->
                if (savedPhone != null) {
                    _uiState.update { it.copy(phone = savedPhone) }
                }
            }
        }
    }

    fun handleKeypadInput(key: String) {
        val currentState = _uiState.value
        when (key) {
            "backspace" -> {
                if (currentState.cardNumber.length < 16 && currentState.expiryDate.isEmpty()) {
                    _uiState.update { it.copy(cardNumber = it.cardNumber.dropLast(1)) }
                } else {
                    if (currentState.expiryDate.isNotEmpty()) {
                        _uiState.update { it.copy(expiryDate = it.expiryDate.dropLast(1)) }
                    } else if (currentState.cardNumber.isNotEmpty()) {
                        _uiState.update { it.copy(cardNumber = it.cardNumber.dropLast(1)) }
                    }
                }
            }

            "clear" -> {
                _uiState.update { it.copy(cardNumber = "", expiryDate = "") }
            }

            else -> {
                if (currentState.cardNumber.length < 16) {
                    _uiState.update { it.copy(cardNumber = it.cardNumber + key) }
                } else if (currentState.expiryDate.length < 4) {
                    _uiState.update { it.copy(expiryDate = it.expiryDate + key) }
                }
            }
        }
    }

    fun addCard() {
        val cardNumber = uiState.value.cardNumber
        val expiryDate = uiState.value.expiryDate

        val formattedExpiryDate = if (expiryDate.length == 4) {
            "${expiryDate.substring(0, 2)}/${expiryDate.substring(2, 4)}"
        } else {
            ""
        }

        if (cardNumber.length != 16 || expiryDate.length != 4) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Invalid card number or expiry date"
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            when (val result = addCardUseCase(cardNumber, formattedExpiryDate)) {
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            message = "Card added successfully",
                        )
                    }
                }

                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                }
            }
        }
    }

    fun clearErrorMessage() {
        _uiState.update {
            it.copy(
                errorMessage = null
            )
        }
    }

    fun clearMessage() {
        _uiState.update {
            it.copy(
                message = null
            )
        }
    }
}
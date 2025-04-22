package com.komzak.wedriveevaluationassignment.presentation.ui.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.local.DataStoreHelper
import com.komzak.wedriveevaluationassignment.domain.model.PaymentMethod
import com.komzak.wedriveevaluationassignment.domain.usecase.ActivatePromoUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.CreateUserUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetUserCardsUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetUserWalletUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.UpdateMethodUseCase
import com.komzak.wedriveevaluationassignment.utils.maskCardNumber
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WalletViewModel(
    private val createUserUseCase: CreateUserUseCase,
    private val activatePromoUseCase: ActivatePromoUseCase,
    private val updateMethodUseCase: UpdateMethodUseCase,
    private val getUserCardsUseCase: GetUserCardsUseCase,
    private val getWalletUseCase: GetUserWalletUseCase,
    private val dataStoreHelper: DataStoreHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(WalletUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getPhoneFromLocale()
    }

    private fun getPhoneFromLocale() {
        viewModelScope.launch {
            dataStoreHelper.getPhoneNumber().collect { savedPhone ->
                if (savedPhone != null) {
                    _uiState.update { it.copy(isIdentificationRequired = false) }
                }
            }
        }
    }

    fun togglePaymentMethod(methodType: PaymentMethod) {
        viewModelScope.launch {
            if (methodType == PaymentMethod.CARD && _uiState.value.activeCardId == -1L) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "You need to add a card first"
                    )
                }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = updateMethodUseCase(methodType, _uiState.value.activeCardId)) {
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            selectedMethod = result.data.activeMethod ?: PaymentMethod.CASH,
                            message = "Payment method changed"
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

    fun onPromoCodeChange(code: String) {
        _uiState.update { it.copy(promoCode = code) }
    }

    fun openPromoSheet() {
        _uiState.update { it.copy(isPromoSheetOpen = true) }
    }

    fun closePromoSheet() {
        _uiState.update { it.copy(isPromoSheetOpen = false, promoCode = "") }
    }

    fun submitPromoCode() {
        if (_uiState.value.promoCode.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Promo code cannot be empty") }
            return
        }
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            when (val result = activatePromoUseCase(_uiState.value.promoCode)) {
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            message = result.data.message
                        )
                    }
                    closePromoSheet()
                }

                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                    closePromoSheet()
                }
            }
        }
    }

    fun onPhoneNumberChange(number: String) {
        if (number.length > 9) return
        _uiState.update { it.copy(phone = number) }
    }

    fun openPhoneSheet() {
        _uiState.update { it.copy(isPhoneSheetOpen = true) }
    }

    fun closePhoneSheet() {
        _uiState.update { it.copy(isPhoneSheetOpen = false, phone = "") }
    }

    fun submitPhoneNumber() {
        val phone = "+998${_uiState.value.phone}"
        if (phone.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Phone number cannot be empty") }
            return
        }
        if (!phone.matches(Regex("^\\+[0-9]{10,15}$"))) {
            _uiState.update { it.copy(errorMessage = "Invalid phone number format") }
            return
        }

        viewModelScope.launch {
            createUser(phone)
            dataStoreHelper.savePhoneNumber(phone)
            _uiState.update { it.copy(isIdentificationRequired = false) }

            getWallets()
            getCards()
            closePhoneSheet()
        }
    }

    fun getWallets() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getWalletUseCase()) {
                is DataResult.Success -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            isSuccess = true,
                            selectedMethod = result.data.activeMethod ?: PaymentMethod.CASH,
                            activeCardId = result.data.activeCardId ?: currentState.activeCardId,
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

    fun getCards() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getUserCardsUseCase()) {
                is DataResult.Success -> {

                    if (result.data.isEmpty()) {
                        _uiState.update {
                            it.copy(
                                isEmptyCards = true,
                                message = "You have no cards",
                            )
                        }
                        return@launch
                    }

                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            isSuccess = true,
                            selectedCard = result.data.firstOrNull()?.number?.maskCardNumber()
                                ?: "",
                            activeCardId = result.data.firstOrNull()?.id
                                ?: currentState.activeCardId
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

    private fun createUser(phone: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = createUserUseCase(phone)) {
                is DataResult.Success -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            isSuccess = true,
                            balance = result.data.balance.toString(),
                            user = result.data,
                            selectedMethod = result.data.activeMethod ?: PaymentMethod.CASH,
                            activeCardId = result.data.activeCardId ?: currentState.activeCardId,
                            message = "User created successfully"
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

    suspend fun logout() {
        dataStoreHelper.clearPhoneNumber()
        _uiState.update {
            it.copy(
                isIdentificationRequired = true,
                selectedMethod = PaymentMethod.CASH,
                selectedCard = "",
                activeCardId = -1,
                user = null,
                isLoading = false,
                errorMessage = null,
                isSuccess = false,
                isEmptyCards = false,
                message = "Logged out"
            )
        }
    }
}
package com.komzak.wedriveevaluationassignment.presentation.ui.createtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.domain.model.BalanceModel
import com.komzak.wedriveevaluationassignment.domain.model.CityModel
import com.komzak.wedriveevaluationassignment.domain.model.CreateTransactionData
import com.komzak.wedriveevaluationassignment.domain.model.UserModel
import com.komzak.wedriveevaluationassignment.domain.usecase.CreateTransactionUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetAllBalanceByIdUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetAllCitiesUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetAllUsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateTransactionUiState(
    val phone: String = "",
    val amount: String = "",
    val serviceFee: String = "", // Added serviceFee
    val details: String = "",
    val type: Int = 1, // 1 for sotish, 2 for olish
    val selectedUserId: Int? = null,
    val selectedBalanceId: Int? = null,
    val selectedCurrencyId: Int? = null,
    val selectedCompanyId: Int? = null,
    val selectedFromCityId: Int? = null,
    val selectedToCityId: Int? = null,
    val receiverName: String = "",
    val users: List<UserModel> = emptyList(),
    val balances: List<BalanceModel> = emptyList(),
    val cities: List<CityModel> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class CreateTransactionViewModel(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getAllBalanceByUserIdUseCase: GetAllBalanceByIdUseCase,
    private val getAllCitiesUseCase: GetAllCitiesUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTransactionUiState())
    val uiState: StateFlow<CreateTransactionUiState> = _uiState.asStateFlow()

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            // Fetch users
            when (val usersResult = getAllUsersUseCase()) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(users = usersResult.data) }
                }

                is DataResult.Error -> {
                    _uiState.update { it.copy(errorMessage = usersResult.error) }
                }
            }

            // Fetch cities
            when (val citiesResult = getAllCitiesUseCase()) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(cities = citiesResult.data) }
                }

                is DataResult.Error -> {
                    _uiState.update { it.copy(errorMessage = citiesResult.error) }
                }
            }
        }
    }

    fun updatePhone(phone: String) {
        _uiState.update { it.copy(phone = phone) }
    }

    fun updateAmount(amount: String) {
        _uiState.update { it.copy(amount = amount) }
    }

    fun updateServiceFee(serviceFee: String) {
        _uiState.update { it.copy(serviceFee = serviceFee) }
    }

    fun updateDetails(details: String) {
        _uiState.update { it.copy(details = details) }
    }

    fun updateReceiverName(name: String) {
        _uiState.update { it.copy(receiverName = name) }
    }

    fun updateType(type: Int) {
        _uiState.update { it.copy(type = type) }
    }

    fun selectUser(userId: Int) {
        _uiState.update { it.copy(selectedUserId = userId) }
        fetchBalances(userId)
    }

    fun selectBalance(balanceId: Int) {
        val selectedBalance = _uiState.value.balances.find { it.id == balanceId }
        _uiState.update {
            it.copy(
                selectedBalanceId = balanceId,
                selectedCurrencyId = selectedBalance?.currencyId,
                selectedCompanyId = selectedBalance?.companyId
            )
        }
    }

    fun selectFromCity(cityId: Int) {
        _uiState.update { it.copy(selectedFromCityId = cityId) }
    }

    fun selectToCity(cityId: Int) {
        _uiState.update { it.copy(selectedToCityId = cityId) }
    }

    private fun fetchBalances(userId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val balancesResult = getAllBalanceByUserIdUseCase(userId)) {
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(
                            balances = balancesResult.data,
                            isLoading = false
                        )
                    }
                }

                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = balancesResult.error,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun createTransaction() {
        val state = _uiState.value
        if (!validateInput(state)) {
            _uiState.update { it.copy(errorMessage = "Please fill all required fields") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val transactionData = CreateTransactionData(
                amount = state.amount.toInt(),
                serviceFee = state.serviceFee.toInt(),
                fromCurrencyTypeId = state.selectedCurrencyId ?: 0,
                toCurrencyTypeId = state.selectedCurrencyId ?: 0, // Same as fromCurrencyTypeId
                senderId = state.selectedUserId ?: 0,
                fromCityId = state.selectedFromCityId ?: 0,
                toCityId = state.selectedToCityId ?: 0,
                receiverId = state.selectedUserId ?: 0, // Assuming sender and receiver can be same
                receiverName = state.receiverName,
                receiverPhone = state.phone,
                details = state.details,
                companyId = state.selectedCompanyId ?: 0,
                balanceId = state.selectedBalanceId ?: 0
            )

            when (val result = createTransactionUseCase(transactionData)) {
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            errorMessage = null
                        )
                    }
                }

                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = false,
                            errorMessage = result.error
                        )
                    }
                }
            }
        }
    }

    private fun validateInput(state: CreateTransactionUiState): Boolean {
        return state.amount.isNotBlank() &&
                state.serviceFee.isNotBlank() &&
                state.phone.isNotBlank() &&
                state.receiverName.isNotBlank() &&
                state.details.isNotBlank() &&
                state.selectedUserId != null &&
                state.selectedBalanceId != null &&
                state.selectedCurrencyId != null &&
                state.selectedCompanyId != null &&
                state.selectedFromCityId != null &&
                state.selectedToCityId != null
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun resetSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}
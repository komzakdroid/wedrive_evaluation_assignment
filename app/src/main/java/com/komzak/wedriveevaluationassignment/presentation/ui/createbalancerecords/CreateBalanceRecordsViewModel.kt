package com.komzak.wedriveevaluationassignment.presentation.ui.createbalancerecords

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.local.DataStoreHelper
import com.komzak.wedriveevaluationassignment.domain.model.BalanceModel
import com.komzak.wedriveevaluationassignment.domain.model.CreateBalanceRecordData
import com.komzak.wedriveevaluationassignment.domain.model.UserModel
import com.komzak.wedriveevaluationassignment.domain.usecase.CreateBalanceRecordsUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetAllBalanceByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateBalanceRecordsUiState(
    val phone: String = "",
    val amount: String = "",
    val details: String = "",
    val type: Int = 1, // 1 for sotish, 2 for olish
    val selectedUserId: Int? = null,
    val selectedBalanceId: Int? = null,
    val selectedCurrencyId: Int? = null,
    val selectedCompanyId: Int? = null,
    val users: List<UserModel> = emptyList(),
    val balances: List<BalanceModel> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class CreateBalanceRecordsViewModel(
    private val getAllBalanceByUserIdUseCase: GetAllBalanceByIdUseCase,
    private val createBalanceRecordsUseCase: CreateBalanceRecordsUseCase,
    private val dataStoreHelper: DataStoreHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateBalanceRecordsUiState())
    val uiState: StateFlow<CreateBalanceRecordsUiState> = _uiState.asStateFlow()

    init {
        fetchUserIds()
    }

    private fun fetchUserIds() {
        viewModelScope.launch {
            dataStoreHelper.getUID().collect { userId ->
                _uiState.update { it.copy(selectedUserId = userId?.toInt()) }
                selectUser(userId?.toInt() ?: 0) // Default to 0 if userId is null
            }
        }
    }

    fun updateAmount(amount: String) {
        _uiState.update { it.copy(amount = amount) }
    }

    fun updateDetails(details: String) {
        _uiState.update { it.copy(details = details) }
    }

    fun updateType(type: Int) {
        _uiState.update { it.copy(type = type) }
    }

    fun selectUser(userId: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedUserId = userId,
                    selectedBalanceId = null, // Reset balance selection
                    selectedCurrencyId = null, // Reset currency selection
                    balances = emptyList() // Clear previous balances
                )
            }

            // Fetch balances for the selected user
            when (val balancesResult = getAllBalanceByUserIdUseCase(userId)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(balances = balancesResult.data) }
                }

                is DataResult.Error -> {
                    _uiState.update { it.copy(errorMessage = balancesResult.error) }
                }
            }
        }
    }

    fun selectBalance(balanceId: Int) {
        val selectedBalance = _uiState.value.balances.find { it.id == balanceId }
        _uiState.update {
            it.copy(
                selectedBalanceId = balanceId,
                selectedCurrencyId = selectedBalance?.currencyId // Set currencyId from selected balance
            )
        }
    }

    fun selectCurrency(currencyId: Int) {
        _uiState.update { it.copy(selectedCurrencyId = currencyId) }
    }

    fun selectCompany(companyId: Int) {
        _uiState.update { it.copy(selectedCompanyId = companyId) }
    }

    fun createBalanceRecord() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.selectedUserId == null || state.selectedBalanceId == null || state.amount.isEmpty()) {
                _uiState.update { it.copy(errorMessage = "Please fill all required fields") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val data = CreateBalanceRecordData(
                amount = state.amount.toInt(),
                userId = state.selectedUserId,
                balanceId = state.selectedBalanceId,
                companyId = state.selectedCompanyId ?: 1,
                details = state.details,
                currencyId = state.selectedCurrencyId
                    ?: 1, // Use selectedCurrencyId set from balance
                type = state.type
            )

            when (val result = createBalanceRecordsUseCase(data)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isSuccess = true, isLoading = false) }
                }

                is DataResult.Error -> {
                    _uiState.update { it.copy(errorMessage = result.error, isLoading = false) }
                }
            }
        }
    }

    fun resetSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
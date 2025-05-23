package com.komzak.wedriveevaluationassignment.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.local.DataStoreHelper
import com.komzak.wedriveevaluationassignment.data.remote.model.request.TransactionRequest
import com.komzak.wedriveevaluationassignment.domain.model.BalanceModel
import com.komzak.wedriveevaluationassignment.domain.model.BalanceRecordModel
import com.komzak.wedriveevaluationassignment.domain.usecase.GetAllBalanceByIdUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetAllBalanceRecordsByIdUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.TransactionCreateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val phone: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val transactionRequest: TransactionRequest? = null,
    val allBalances: List<BalanceModel> = emptyList(),
    val balanceRecords: Map<Int, List<BalanceRecordModel>> = emptyMap(),
    val selectedBalanceId: Int? = null
)

class HomeViewModel(
    private val getAllBalanceUseCase: GetAllBalanceByIdUseCase,
    private val transactionCreateUseCase: TransactionCreateUseCase,
    private val getAllBalanceRecordsByIdUseCase: GetAllBalanceRecordsByIdUseCase,
    private val dataStoreHelper: DataStoreHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getPhoneFromLocale()
        getAllBalance()
    }

    fun refresh() {
        getAllBalance()
    }


    fun selectBalance(balanceId: Int) {
        _uiState.update { it.copy(selectedBalanceId = balanceId) }
        getBalanceRecords(balanceId)
    }

    fun createTransaction() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val transaction = _uiState.value.transactionRequest
            val request = TransactionRequest(
                amount = transaction?.amount ?: 0,
                serviceFee = transaction?.serviceFee ?: 0,
                fromCurrencyTypeId = transaction?.fromCurrencyTypeId ?: 0,
                toCurrencyTypeId = transaction?.toCurrencyTypeId ?: 0,
                senderId = transaction?.senderId ?: 0,
                fromCityId = transaction?.fromCityId ?: 0,
                toCityId = transaction?.toCityId ?: 0,
                receiverId = transaction?.receiverId ?: 0,
                receiverName = transaction?.receiverName ?: "",
                receiverPhone = transaction?.receiverPhone ?: "",
                details = transaction?.details ?: "",
                type = transaction?.type ?: 1,
                companyId = transaction?.companyId ?: 1,
                balanceId = transaction?.balanceId ?: 0
            )
            when (val result = transactionCreateUseCase(request)) {
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            errorMessage = null
                        )
                    }
                    // Refresh data after successful transaction
                    getAllBalance()
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

    private fun getBalanceRecords(balanceId: Int) {
        viewModelScope.launch {
            when (val result = getAllBalanceRecordsByIdUseCase(balanceId)) {
                is DataResult.Success -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            balanceRecords = currentState.balanceRecords + (balanceId to result.data)
                        )
                    }
                }
                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(errorMessage = result.error)
                    }
                }
            }
        }
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

    private fun getAllBalance() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            dataStoreHelper.getUID().collect { userId ->
                if (userId != null) {
                    when (val result = getAllBalanceUseCase(userId.toInt())) {
                        is DataResult.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    isSuccess = true,
                                    allBalances = result.data,
                                    errorMessage = null
                                )
                            }

                            if (result.data.isNotEmpty()) {
                                selectBalance(result.data.first().id ?: 0)
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
        }
    }
}
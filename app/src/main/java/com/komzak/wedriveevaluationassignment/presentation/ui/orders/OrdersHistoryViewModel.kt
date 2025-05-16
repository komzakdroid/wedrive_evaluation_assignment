package com.komzak.wedriveevaluationassignment.presentation.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.local.DataStoreHelper
import com.komzak.wedriveevaluationassignment.domain.model.TransactionModel
import com.komzak.wedriveevaluationassignment.domain.usecase.GetTransactionsByBalanceIdUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetTransactionsByDateUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetTransactionsByStatusUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetTransactionsByUserIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OrdersHistoryUiState(
    val phone: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val transactions: List<TransactionModel> = emptyList(),
    val selectedUserId: Int? = null,
    val selectedBalanceId: Int? = null,
    val selectedStatus: Long? = null,
    val selectedDateRange: Pair<String, String>? = null,
    val availableUserIds: List<Int> = emptyList(),
    val availableBalanceIds: List<Int> = emptyList()
)

class OrdersHistoryViewModel(
    private val getTransactionsByBalanceIdUseCase: GetTransactionsByBalanceIdUseCase,
    private val getTransactionsByUserIdUseCase: GetTransactionsByUserIdUseCase,
    private val getTransactionsByStatusUseCase: GetTransactionsByStatusUseCase,
    private val getTransactionsByDateUseCase: GetTransactionsByDateUseCase,
    private val dataStoreHelper: DataStoreHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrdersHistoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getPhoneFromLocale()
        fetchFilterData()
        fetchTransactions()
    }

    fun refresh() {
        fetchTransactions()
    }

    fun updateUserId(userId: Int?) {
        _uiState.update {
            it.copy(
                selectedUserId = userId,
                selectedBalanceId = null,
                selectedStatus = null,
                selectedDateRange = null
            )
        }
        fetchTransactions()
    }

    fun updateBalanceId(balanceId: Int?) {
        _uiState.update {
            it.copy(
                selectedBalanceId = balanceId,
                selectedUserId = null,
                selectedStatus = null,
                selectedDateRange = null
            )
        }
        fetchTransactions()
    }

    fun updateStatus(status: Long?) {
        _uiState.update {
            it.copy(
                selectedStatus = status,
                selectedUserId = null,
                selectedBalanceId = null,
                selectedDateRange = null
            )
        }
        fetchTransactions()
    }

    fun updateDateRange(from: String, to: String) {
        _uiState.update {
            it.copy(
                selectedDateRange = from to to,
                selectedUserId = null,
                selectedBalanceId = null,
                selectedStatus = null
            )
        }
        fetchTransactions()
    }

    fun clearFilters() {
        _uiState.update {
            it.copy(
                selectedUserId = null,
                selectedBalanceId = null,
                selectedStatus = null,
                selectedDateRange = null
            )
        }
        fetchTransactions()
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

    private fun fetchFilterData() {
        // TODO: Fetch dynamic user IDs and balance IDs from API or local storage
        // Placeholder implementation
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    availableUserIds = listOf(1, 2, 3), // Replace with actual data
                    availableBalanceIds = listOf(101, 102, 103) // Replace with actual data
                )
            }
        }
    }

    private fun fetchTransactions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = when {
                _uiState.value.selectedDateRange != null -> {
                    val (from, to) = _uiState.value.selectedDateRange!!
                    // Assuming API accepts a single date or range in a specific format
                    getTransactionsByDateUseCase("$from-$to") // Adjust format based on API
                }

                _uiState.value.selectedStatus != null -> {
                    getTransactionsByStatusUseCase(_uiState.value.selectedStatus.toString())
                }

                _uiState.value.selectedBalanceId != null -> {
                    getTransactionsByBalanceIdUseCase(_uiState.value.selectedBalanceId!!)
                }

                _uiState.value.selectedUserId != null -> {
                    getTransactionsByUserIdUseCase(_uiState.value.selectedUserId!!)
                }

                else -> {
                    val userId = dataStoreHelper.getUID().first()
                    if (userId != null) {
                        getTransactionsByUserIdUseCase(userId.toInt())
                    } else {
                        DataResult.Error("User ID not found")
                    }
                }
            }

            when (result) {
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            transactions = result.data,
                            errorMessage = null
                        )
                    }
                }

                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            transactions = emptyList(),
                            errorMessage = result.error
                        )
                    }
                }
            }
        }
    }
}
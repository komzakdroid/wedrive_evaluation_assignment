package com.komzak.wedriveevaluationassignment.presentation.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.local.DataStoreHelper
import com.komzak.wedriveevaluationassignment.domain.model.TransactionModel
import com.komzak.wedriveevaluationassignment.domain.usecase.CompleteActionByIdUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetAllBalanceUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetAllUsersUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetTransactionsByBalanceIdUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetTransactionsByDateUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetTransactionsByReceiverIdUseCase
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
    val selectedReceiverId: Int? = null,
    val selectedBalanceId: Int? = null,
    val selectedStatus: Long? = null,
    val selectedDateRange: Pair<String, String>? = null,
    val availableUserIds: List<Int> = emptyList(),
    val availableBalanceIds: List<Int?> = emptyList()
)

class OrdersHistoryViewModel(
    private val getTransactionsByBalanceIdUseCase: GetTransactionsByBalanceIdUseCase,
    private val getTransactionsByUserIdUseCase: GetTransactionsByUserIdUseCase,
    private val getTransactionsByReceiverIdUseCase: GetTransactionsByReceiverIdUseCase,
    private val getTransactionsByStatusUseCase: GetTransactionsByStatusUseCase,
    private val getTransactionsByDateUseCase: GetTransactionsByDateUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getAllBalancesUseCase: GetAllBalanceUseCase,
    private val completeActionByIdUseCase: CompleteActionByIdUseCase,
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
                selectedReceiverId = null,
                selectedBalanceId = null,
                selectedStatus = null,
                selectedDateRange = null
            )
        }
        fetchTransactions()
    }

    fun updateReceiverId(receiverId: Int?) {
        _uiState.update {
            it.copy(
                selectedReceiverId = receiverId,
                selectedUserId = null,
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
                selectedReceiverId = null,
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
                selectedReceiverId = null,
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
                selectedReceiverId = null,
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
                selectedReceiverId = null,
                selectedBalanceId = null,
                selectedStatus = null,
                selectedDateRange = null
            )
        }
        fetchTransactions()
    }

    fun completeTransaction(serialNo: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = completeActionByIdUseCase(serialNo)
            when (result) {
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    fetchTransactions() // Refresh transactions after completion
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
        viewModelScope.launch {
            // Fetch user IDs
            val userResult = getAllUsersUseCase()
            val userIds = when (userResult) {
                is DataResult.Success -> userResult.data.map { it.id }
                is DataResult.Error -> emptyList()
            }

            // Fetch balance IDs
            val balanceResult = getAllBalancesUseCase()
            val balanceIds = when (balanceResult) {
                is DataResult.Success -> balanceResult.data.map { it.id }
                is DataResult.Error -> emptyList()
            }

            _uiState.update {
                it.copy(
                    availableUserIds = userIds,
                    availableBalanceIds = balanceIds
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
                    getTransactionsByDateUseCase("$from-$to")
                }

                _uiState.value.selectedStatus != null -> {
                    getTransactionsByStatusUseCase(_uiState.value.selectedStatus.toString())
                }

                _uiState.value.selectedBalanceId != null -> {
                    getTransactionsByBalanceIdUseCase(_uiState.value.selectedBalanceId!!)
                }

                _uiState.value.selectedReceiverId != null -> {
                    getTransactionsByReceiverIdUseCase(_uiState.value.selectedReceiverId!!)
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
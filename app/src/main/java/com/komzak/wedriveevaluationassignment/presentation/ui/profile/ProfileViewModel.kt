package com.komzak.wedriveevaluationassignment.presentation.ui.profile

import androidx.lifecycle.ViewModel
import com.komzak.wedriveevaluationassignment.data.local.DataStoreHelper
import com.komzak.wedriveevaluationassignment.data.remote.model.request.TransactionRequest
import com.komzak.wedriveevaluationassignment.domain.model.BalanceModel
import com.komzak.wedriveevaluationassignment.domain.model.BalanceRecordModel
import com.komzak.wedriveevaluationassignment.domain.usecase.GetAllBalanceByIdUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetAllBalanceRecordsByIdUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.TransactionCreateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


data class ProfileUiState(
    val phone: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val transactionRequest: TransactionRequest? = null,
    val allBalances: List<BalanceModel> = emptyList(),
    val balanceRecords: Map<Int, List<BalanceRecordModel>> = emptyMap(),
    val selectedBalanceId: Int? = null
)

class ProfileViewModel(
    private val getAllBalanceUseCase: GetAllBalanceByIdUseCase,
    private val transactionCreateUseCase: TransactionCreateUseCase,
    private val getAllBalanceRecordsByIdUseCase: GetAllBalanceRecordsByIdUseCase,
    private val dataStoreHelper: DataStoreHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()
}
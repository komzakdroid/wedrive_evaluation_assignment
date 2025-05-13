package com.komzak.wedriveevaluationassignment.presentation.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.local.DataStoreHelper
import com.komzak.wedriveevaluationassignment.domain.model.BalanceRecordModel
import com.komzak.wedriveevaluationassignment.domain.usecase.GetBalanceRecordsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HistoryUiState(
    val phone: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val records: List<BalanceRecordModel> = emptyList()
)

class HistoryViewModel(
    private val getBalanceRecordsUseCase: GetBalanceRecordsUseCase,
    private val dataStoreHelper: DataStoreHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getBalanceRecords()
        getPhoneFromLocale()
    }

    fun refresh() {
        getBalanceRecords()
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

    private fun getBalanceRecords() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            dataStoreHelper.getUID().collect { userId ->
                if (userId != null) {
                    when (val result = getBalanceRecordsUseCase(userId)) {
                        is DataResult.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    isSuccess = true,
                                    records = result.data,
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
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = false,
                            errorMessage = "User ID not found"
                        )
                    }
                }
            }
        }
    }
}
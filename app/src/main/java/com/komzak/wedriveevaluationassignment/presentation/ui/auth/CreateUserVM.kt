package com.komzak.wedriveevaluationassignment.presentation.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.domain.usecase.CreateUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateUserVM(private val createUserUseCase: CreateUserUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateUserUiState())
    val uiState = _uiState.asStateFlow()

    fun createUser(phone: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = createUserUseCase(phone)) {
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true
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

    fun onPhoneEdit(rawInput: String) {
        // Filter out non-digits and limit to 9 digits
        val digits = rawInput.filter { it.isDigit() }.take(9)

        // Format the phone number as 00 000 00 00
        val formattedPhone = formatPhoneNumber(digits)

        // Validate: check if the input is exactly 9 digits
        val errorMessage = if (digits.length < 9) {
            "Phone number must be 9 digits"
        } else {
            null
        }

        _uiState.update {
            it.copy(
                phone = formattedPhone,
                errorMessage = errorMessage
            )
        }
    }

    private fun formatPhoneNumber(digits: String): String {
        return when (digits.length) {
            0 -> ""
            in 1..2 -> digits
            in 3..5 -> "${digits.substring(0, 2)} ${digits.substring(2)}"
            in 6..7 -> "${digits.substring(0, 2)} ${digits.substring(2, 5)} ${digits.substring(5)}"
            else -> "${digits.substring(0, 2)} ${digits.substring(2, 5)} ${
                digits.substring(
                    5,
                    7
                )
            } ${digits.substring(7)}"
        }
    }
}
package com.komzak.wedriveevaluationassignment.presentation.ui.auth

import com.komzak.wedriveevaluationassignment.domain.model.UserModel

data class CreateUserUiState(
    val phone: String = "",
    val user: UserModel? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)
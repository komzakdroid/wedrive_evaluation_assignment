package com.komzak.wedriveevaluationassignment.data.repository.user.register

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserRegisterResponse

interface CreateUserRepository {
    suspend fun register(
        username: String,
        phone: String,
        role: Int,
        password: String
    ): DataResult<UserRegisterResponse, AppError>
}

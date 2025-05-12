package com.komzak.wedriveevaluationassignment.data.repository.user.login

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserLoginResponse

interface LoginUserRepository {
    suspend fun login(
        phone: String,
        password: String
    ): DataResult<UserLoginResponse, AppError>
}

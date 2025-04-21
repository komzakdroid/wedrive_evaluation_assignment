package com.komzak.wedriveevaluationassignment.data.repository.user

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserResponse

interface CreateUserRepository {
    suspend fun createUser(phone: String): DataResult<UserResponse, AppError>
}

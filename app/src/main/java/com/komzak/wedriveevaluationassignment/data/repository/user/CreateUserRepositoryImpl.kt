package com.komzak.wedriveevaluationassignment.data.repository.user

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.model.request.UserRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserResponse
import com.komzak.wedriveevaluationassignment.utils.executeApiCall

class CreateUserRepositoryImpl(private val api: WeDriveApi) : CreateUserRepository {

    override suspend fun createUser(phone: String): DataResult<UserResponse, AppError> {
        return executeApiCall(
            call = { api.createUser(UserRequest(phone)) }
        )
    }
}
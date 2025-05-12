package com.komzak.wedriveevaluationassignment.data.repository.user.register

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.model.request.UserRegisterRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserRegisterResponse
import com.komzak.wedriveevaluationassignment.utils.executeApiCall

class CreateUserRepositoryImpl(private val api: WeDriveApi) : CreateUserRepository {

    override suspend fun register(
        username: String,
        phone: String,
        role: Int,
        password: String
    ): DataResult<UserRegisterResponse, AppError> {
        return executeApiCall(
            call = {
                api.register(
                    UserRegisterRequest(
                        username = username,
                        phone = phone,
                        password = password,
                        role = role
                    )
                )
            }
        )
    }
}
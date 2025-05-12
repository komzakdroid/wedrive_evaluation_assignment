package com.komzak.wedriveevaluationassignment.data.repository.user.login

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.model.request.UserLoginRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserLoginResponse
import com.komzak.wedriveevaluationassignment.utils.executeApiCall

class LoginUserRepositoryImpl(private val api: WeDriveApi) : LoginUserRepository {
    override suspend fun login(
        phone: String,
        password: String
    ): DataResult<UserLoginResponse, AppError> {
        return executeApiCall(
            call = {
                api.login(
                    UserLoginRequest(
                        phone = phone,
                        password = password
                    )
                )
            }
        )
    }
}
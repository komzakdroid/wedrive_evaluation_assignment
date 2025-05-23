package com.komzak.wedriveevaluationassignment.data.repository.dashboard

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.model.response.AllUsersResponse
import com.komzak.wedriveevaluationassignment.utils.executeApiCall

class GetAllUsersRepositoryImpl(private val api: WeDriveApi) : GetAllUsersRepository {
    override suspend fun getAllUsers(): DataResult<AllUsersResponse, AppError> {
        return executeApiCall(
            call = {
                api.getAllUsers()
            }
        )
    }
}
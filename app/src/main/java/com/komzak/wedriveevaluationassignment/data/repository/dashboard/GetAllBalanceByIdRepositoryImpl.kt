package com.komzak.wedriveevaluationassignment.data.repository.dashboard

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.model.response.AllBalanceResponse
import com.komzak.wedriveevaluationassignment.utils.executeApiCall

class GetAllBalanceByIdRepositoryImpl(private val api: WeDriveApi) : GetAllBalanceByIdRepository {
    override suspend fun getAllBalance(userId: Int): DataResult<AllBalanceResponse, AppError> {
        return executeApiCall(
            call = {
                api.getAllBalanceById(userId)
            }
        )
    }
}
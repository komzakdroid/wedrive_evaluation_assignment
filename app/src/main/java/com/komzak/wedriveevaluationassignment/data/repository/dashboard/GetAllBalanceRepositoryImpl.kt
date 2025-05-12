package com.komzak.wedriveevaluationassignment.data.repository.dashboard

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.model.response.AllBalanceResponse
import com.komzak.wedriveevaluationassignment.utils.executeApiCall

class GetAllBalanceRepositoryImpl(private val api: WeDriveApi) : GetAllBalanceRepository {
    override suspend fun getAllBalance(): DataResult<AllBalanceResponse, AppError> {
        return executeApiCall(
            call = {
                api.getAllBalance()
            }
        )
    }
}
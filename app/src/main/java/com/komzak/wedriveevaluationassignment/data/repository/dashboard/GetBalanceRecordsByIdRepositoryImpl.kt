package com.komzak.wedriveevaluationassignment.data.repository.dashboard

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.model.response.BalanceRecordsResponse
import com.komzak.wedriveevaluationassignment.utils.executeApiCall

class GetAllBalanceRecordsByIdRepositoryImpl(private val api: WeDriveApi) : GetAllBalanceRecordsByIdRepository {
    override suspend fun getBalanceRecordsById(balanceId: Int): DataResult<BalanceRecordsResponse, AppError> {
        return executeApiCall(
            call = {
                api.getAllBalanceRecordsById(balanceId)
            }
        )
    }
}
package com.komzak.wedriveevaluationassignment.data.repository.dashboard

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.model.request.CreateBalanceRecordsRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.response.CreateBalanceRecordsResponse
import com.komzak.wedriveevaluationassignment.utils.executeApiCall

class CreateBalanceRecordsRepositoryImpl(private val api: WeDriveApi) :
    CreateBalanceRecordsRepository {
    override suspend fun createRecord(request: CreateBalanceRecordsRequest): DataResult<CreateBalanceRecordsResponse, AppError> {
        return executeApiCall(
            call = {
                api.createBalanceRecords(request)
            }
        )
    }
}
package com.komzak.wedriveevaluationassignment.data.repository.dashboard

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.model.request.CreateBalanceRecordsRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.response.BalanceRecordsResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.CreateBalanceRecordsResponse

interface CreateBalanceRecordsRepository {
    suspend fun createRecord(
        request: CreateBalanceRecordsRequest
    ): DataResult<CreateBalanceRecordsResponse, AppError>
}
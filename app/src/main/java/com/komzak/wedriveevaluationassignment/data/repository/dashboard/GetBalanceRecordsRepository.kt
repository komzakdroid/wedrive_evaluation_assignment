package com.komzak.wedriveevaluationassignment.data.repository.dashboard

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.model.response.BalanceRecordsResponse

interface GetBalanceRecordsRepository {
    suspend fun getBalanceRecords(
        uui: Int
    ): DataResult<BalanceRecordsResponse, AppError>
}

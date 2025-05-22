package com.komzak.wedriveevaluationassignment.data.repository.dashboard

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.model.response.AllBalanceResponse

interface GetAllBalanceByIdRepository {
    suspend fun getAllBalance(
        userId: Int
    ): DataResult<AllBalanceResponse, AppError>
}

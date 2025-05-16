package com.komzak.wedriveevaluationassignment.data.repository.dashboard

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.model.response.TransactionResponse

interface TransactionsByDateRepository {
    suspend fun getTransactionsByDate(
        date: String
    ): DataResult<TransactionResponse, AppError>
}
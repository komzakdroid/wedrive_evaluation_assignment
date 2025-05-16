package com.komzak.wedriveevaluationassignment.data.repository.dashboard

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.model.request.TransactionRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.response.TransactionCreateResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.TransactionResponse

interface TransactionCreateRepository {
    suspend fun createTransaction(
        request: TransactionRequest
    ): DataResult<TransactionCreateResponse, AppError>
}

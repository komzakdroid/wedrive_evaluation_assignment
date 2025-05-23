package com.komzak.wedriveevaluationassignment.data.repository.dashboard

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.model.request.CreateBalanceRecordsRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.request.CreateTransactionRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.response.BalanceRecordsResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.CreateTransactionResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.TransactionResponse

interface CreateTransactionRepository {
    suspend fun createTransaction(
        request: CreateTransactionRequest
    ): DataResult<CreateTransactionResponse, AppError>
}
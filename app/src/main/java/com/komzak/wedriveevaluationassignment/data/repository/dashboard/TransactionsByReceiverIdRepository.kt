package com.komzak.wedriveevaluationassignment.data.repository.dashboard

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.model.response.TransactionResponse

interface TransactionsByReceiverIdRepository {
    suspend fun getTransactionsByReceiverId(
        receiverId: Int
    ): DataResult<TransactionResponse, AppError>
}
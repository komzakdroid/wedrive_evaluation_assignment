package com.komzak.wedriveevaluationassignment.data.repository.dashboard

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.model.response.TransactionResponse
import com.komzak.wedriveevaluationassignment.utils.executeApiCall

class TransactionsByReceiverIdRepositoryImpl(private val api: WeDriveApi) :
    TransactionsByReceiverIdRepository {
    override suspend fun getTransactionsByReceiverId(receiverId: Int): DataResult<TransactionResponse, AppError> {
        return executeApiCall(
            call = {
                api.getTransactionsByReceiverId(receiverId)
            }
        )
    }
}
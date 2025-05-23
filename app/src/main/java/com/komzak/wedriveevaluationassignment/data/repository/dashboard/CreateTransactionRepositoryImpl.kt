package com.komzak.wedriveevaluationassignment.data.repository.dashboard

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.model.request.CreateTransactionRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.response.CreateTransactionResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.TransactionResponse
import com.komzak.wedriveevaluationassignment.utils.executeApiCall

class CreateTransactionRepositoryImpl(private val api: WeDriveApi) :
    CreateTransactionRepository {
    override suspend fun createTransaction(request: CreateTransactionRequest): DataResult<CreateTransactionResponse, AppError> {
        return executeApiCall(
            call = {
                api.createTransaction(request)
            }
        )
    }
}
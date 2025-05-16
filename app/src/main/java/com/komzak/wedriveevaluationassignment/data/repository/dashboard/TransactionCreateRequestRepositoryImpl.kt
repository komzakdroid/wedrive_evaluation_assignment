package com.komzak.wedriveevaluationassignment.data.repository.dashboard

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.model.request.TransactionRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.response.TransactionCreateResponse
import com.komzak.wedriveevaluationassignment.utils.executeApiCall

class TransactionCreateRequestRepositoryImpl(private val api: WeDriveApi) :
    TransactionCreateRepository {
    override suspend fun createTransaction(request: TransactionRequest): DataResult<TransactionCreateResponse, AppError> {
        return executeApiCall(
            call = {
                api.createTransaction(request)
            }
        )
    }
}
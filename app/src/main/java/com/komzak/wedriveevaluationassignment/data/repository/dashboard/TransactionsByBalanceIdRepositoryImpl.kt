package com.komzak.wedriveevaluationassignment.data.repository.dashboard

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.model.response.TransactionResponse
import com.komzak.wedriveevaluationassignment.utils.executeApiCall

class TransactionsByBalanceIdRepositoryImpl(private val api: WeDriveApi) : TransactionsByBalanceIdRepository {
    override suspend fun getTransactionsByBalanceId(balanceId: Int): DataResult<TransactionResponse, AppError> {
        return executeApiCall(
            call = {
                api.getTransactionsByBalanceId(balanceId)
            }
        )
    }
}
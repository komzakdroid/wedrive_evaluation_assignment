package com.komzak.wedriveevaluationassignment.data.repository.dashboard

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.model.response.CompleteActionResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.TransactionResponse

interface CompleteActionByIdRepository {
    suspend fun completeAction(
        actionId: String
    ): DataResult<CompleteActionResponse, AppError>
}
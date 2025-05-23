package com.komzak.wedriveevaluationassignment.data.repository.dashboard

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.model.response.CompleteActionResponse
import com.komzak.wedriveevaluationassignment.utils.executeApiCall

class CompleteActionByIdRepositoryImpl(private val api: WeDriveApi) : CompleteActionByIdRepository {
    override suspend fun completeAction(actionId: String): DataResult<CompleteActionResponse, AppError> {
        return executeApiCall(
            call = {
                api.completeActionById(actionId)
            }
        )
    }
}
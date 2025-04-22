package com.komzak.wedriveevaluationassignment.data.repository.method

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserResponse
import com.komzak.wedriveevaluationassignment.domain.model.PaymentMethod

interface UpdateMethodRepository {
    suspend fun updateMethod(
        payment: PaymentMethod,
        cardId: Long?
    ): DataResult<UserResponse, AppError>
}
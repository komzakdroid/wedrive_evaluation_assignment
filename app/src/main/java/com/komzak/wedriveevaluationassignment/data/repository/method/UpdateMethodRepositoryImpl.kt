package com.komzak.wedriveevaluationassignment.data.repository.method

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.local.DataStoreHelper
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.model.request.MethodRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserResponse
import com.komzak.wedriveevaluationassignment.domain.model.PaymentMethod
import com.komzak.wedriveevaluationassignment.utils.executeApiCall
import kotlinx.coroutines.flow.firstOrNull

class UpdateMethodRepositoryImpl(
    private val api: WeDriveApi,
    private val dataStoreHelper:
    DataStoreHelper
) : UpdateMethodRepository {
    override suspend fun updateMethod(
        payment: PaymentMethod,
        cardId: Long?,
    ): DataResult<UserResponse, AppError> {
        val phone = dataStoreHelper.getPhoneNumber().firstOrNull() ?: ""
        return executeApiCall(
            call = { api.updateMethod(MethodRequest(payment.method, cardId), phone) }
        )
    }
}
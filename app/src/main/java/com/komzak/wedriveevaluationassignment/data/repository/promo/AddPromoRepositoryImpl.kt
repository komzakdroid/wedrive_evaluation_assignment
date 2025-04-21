package com.komzak.wedriveevaluationassignment.data.repository.promo

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.model.request.PromoRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.response.PromoResponse
import com.komzak.wedriveevaluationassignment.utils.executeApiCall

class AddPromoRepositoryImpl(private val api: WeDriveApi) : AddPromoRepository {
    override suspend fun addPromo(code: String): DataResult<PromoResponse, AppError> {
        return executeApiCall(
            call = { api.addPromo(PromoRequest(code)) }
        )
    }
}
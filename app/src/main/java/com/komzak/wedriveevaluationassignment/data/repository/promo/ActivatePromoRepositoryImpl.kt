package com.komzak.wedriveevaluationassignment.data.repository.promo

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.local.DataStoreHelper
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.model.request.PromoRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.response.PromoResponse
import com.komzak.wedriveevaluationassignment.utils.executeApiCall
import kotlinx.coroutines.flow.firstOrNull

class ActivatePromoRepositoryImpl(
    private val api: WeDriveApi,
    private val dataStoreHelper: DataStoreHelper
) : ActivatePromoRepository {
    override suspend fun activatePromo(
        code: String
    ): DataResult<PromoResponse, AppError> {
        val phone = dataStoreHelper.getPhoneNumber().firstOrNull() ?: ""

        return executeApiCall(
            call = { api.activatePromo(PromoRequest(code), phone) }
        )
    }
}
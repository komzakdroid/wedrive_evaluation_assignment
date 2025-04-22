package com.komzak.wedriveevaluationassignment.data.repository.promo

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.model.response.PromoResponse

interface ActivatePromoRepository {
    suspend fun activatePromo(code: String): DataResult<PromoResponse, AppError>
}

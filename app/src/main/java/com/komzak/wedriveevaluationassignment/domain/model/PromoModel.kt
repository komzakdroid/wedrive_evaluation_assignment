package com.komzak.wedriveevaluationassignment.domain.model

import com.komzak.wedriveevaluationassignment.data.remote.model.response.PromoResponse

data class PromoModel(
    val message: String?
)

fun PromoResponse.toDomain(): PromoModel {
    return PromoModel(
        message = message
    )
}
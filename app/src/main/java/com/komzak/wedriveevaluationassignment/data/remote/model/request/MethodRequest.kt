package com.komzak.wedriveevaluationassignment.data.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MethodRequest(
    @SerialName("active_method") val activeMethod: String,
    @SerialName("active_card_id") val activeCardId: Long?,
)
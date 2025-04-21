package com.komzak.wedriveevaluationassignment.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Int?,
    val phone: String?,
    val balance: Double?,
    @SerialName("active_method") val activeMethod: String?,
    @SerialName("active_card_id") val activeCardId: Int?
)
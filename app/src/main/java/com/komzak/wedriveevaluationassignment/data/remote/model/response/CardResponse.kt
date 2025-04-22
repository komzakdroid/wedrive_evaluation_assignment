package com.komzak.wedriveevaluationassignment.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardResponse(
    val id: Long,
    val number: String,
    @SerialName("expire_date") val expireDate: String,
    @SerialName("user_id") val userId: Long
)
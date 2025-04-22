package com.komzak.wedriveevaluationassignment.data.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardRequest(
    @SerialName("number") val number: String,
    @SerialName("expire_date") val expireDate: String?,
)
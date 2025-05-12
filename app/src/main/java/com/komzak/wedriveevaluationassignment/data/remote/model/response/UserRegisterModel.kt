package com.komzak.wedriveevaluationassignment.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDataResponse(
    val id: Int?,
    val username: String?,
    val phone: String?,
    val password: String?,
    val role: Int?,
    @SerialName("created_at") val createdAt: String?,
    val avatar: String?
)
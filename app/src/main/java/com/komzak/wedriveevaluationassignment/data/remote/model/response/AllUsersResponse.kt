package com.komzak.wedriveevaluationassignment.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllUsersResponse(
    val data: List<UserResponse>?,
)

@Serializable
data class UserResponse(
    val id: Int?,
    val username: String?,
    val phone: String?,
    val role: Int?,
    val password: String?,
    val avatar: String? = null,
    @SerialName("created_at") val createdAt: String?,
)
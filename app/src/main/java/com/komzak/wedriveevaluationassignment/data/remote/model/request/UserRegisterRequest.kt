package com.komzak.wedriveevaluationassignment.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterRequest(
    val username: String,
    val phone: String,
    val password: String,
    val role: Int
)
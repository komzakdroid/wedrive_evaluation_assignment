package com.komzak.wedriveevaluationassignment.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginRequest(
    val phone: String,
    val password: String
)
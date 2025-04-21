package com.komzak.wedriveevaluationassignment.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val phone: String
)
package com.komzak.wedriveevaluationassignment.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginResponse(
    val data: UserLoginDataResponse?
)

@Serializable
data class UserLoginDataResponse(
    val token: String?,
    val user: UserDataResponse?
)
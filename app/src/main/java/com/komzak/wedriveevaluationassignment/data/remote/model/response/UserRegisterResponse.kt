package com.komzak.wedriveevaluationassignment.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterResponse(
    val data: UserDataResponse?
)

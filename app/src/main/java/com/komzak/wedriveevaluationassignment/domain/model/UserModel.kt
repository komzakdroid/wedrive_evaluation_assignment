package com.komzak.wedriveevaluationassignment.domain.model

import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserResponse

data class UserModel(
    val id: Int?,
    val phone: String?,
    val balance: Double?,
    val activeMethod: String?,
    val activeCardId: Int?
)

fun UserResponse.toDomain(): UserModel {
    return UserModel(
        id = id,
        phone = phone,
        balance = balance,
        activeMethod = activeMethod,
        activeCardId = activeCardId
    )
}
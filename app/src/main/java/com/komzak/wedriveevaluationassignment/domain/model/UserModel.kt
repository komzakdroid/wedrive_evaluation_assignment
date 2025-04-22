package com.komzak.wedriveevaluationassignment.domain.model

import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserResponse

data class UserModel(
    val id: Int?,
    val phone: String?,
    val balance: Double?,
    val activeMethod: PaymentMethod?,
    val activeCardId: Long?
)

fun UserResponse.toDomain(): UserModel {
    return UserModel(
        id = id,
        phone = phone,
        balance = balance,
        activeMethod = activeMethod?.let { PaymentMethod.fromString(it) },
        activeCardId = activeCardId
    )
}

enum class PaymentMethod(val method: String) {
    CASH("cash"),
    CARD("card");

    companion object {
        fun fromString(method: String) =
            PaymentMethod.entries.firstOrNull { it.method == method } ?: CASH
    }
}
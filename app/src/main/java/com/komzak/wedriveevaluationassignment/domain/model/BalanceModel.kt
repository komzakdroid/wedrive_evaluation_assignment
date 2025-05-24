package com.komzak.wedriveevaluationassignment.domain.model

import com.komzak.wedriveevaluationassignment.data.remote.model.response.BalanceResponse

data class BalanceModel(
    val id: Int?,
    val balance: Double?,
    val userId: Int?,
    val inOutLay: Double?,
    val outInLay: Double?,
    val companyId: Int?,
    val currencyId: Int?,
    val currencyType: String?,
    val createdAt: String?,
    val updatedAt: String?
)

fun BalanceResponse.toDomain(): BalanceModel {
    return BalanceModel(
        id = id,
        balance = balance,
        userId = userId,
        inOutLay = inOutLay,
        outInLay = outInLay,
        companyId = companyId,
        currencyId = currencyId,
        currencyType = currencyType + "(${addCurrencyChar(currencyType)})",
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun addCurrencyChar(currencyType: String?): String {
    return when (currencyType) {
        "USD" -> "$"
        "EUR" -> "€"
        "RUB" -> "₽"
        "UZS" -> "сўм"
        else -> ""
    }
}
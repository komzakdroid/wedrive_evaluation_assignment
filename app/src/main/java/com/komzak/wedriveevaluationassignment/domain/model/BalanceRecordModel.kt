package com.komzak.wedriveevaluationassignment.domain.model

import com.komzak.wedriveevaluationassignment.data.remote.model.response.BalanceRecordsItemResponse

data class BalanceRecordModel(
    val id: Int? = null,
    val amount: Double? = null,
    val serialNo: String? = null,
    val userId: Int? = null,
    val balanceId: Int? = null,
    val details: String? = null,
    val type: Int? = null,
    val currencyId: Int? = null,
    val currencyType: String? = null,
    val createdAt: String? = null,
)

fun BalanceRecordsItemResponse.toDomain(): BalanceRecordModel {
    return BalanceRecordModel(
        id = id,
        amount = amount,
        serialNo = serialNo,
        userId = userId,
        balanceId = balanceId,
        details = details,
        type = type,
        currencyId = currencyId,
        currencyType = currencyType,
        createdAt = createdAt
    )
}
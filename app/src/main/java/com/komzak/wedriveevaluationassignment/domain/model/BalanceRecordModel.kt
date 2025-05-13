package com.komzak.wedriveevaluationassignment.domain.model

import com.komzak.wedriveevaluationassignment.data.remote.model.response.BalanceRecordsItemResponse

data class BalanceRecordModel(
    val id: Int?,
    val amount: Double?,
    val serialNo: String?,
    val userId: Int?,
    val balanceId: Int?,
    val details: String?,
    val type: Int?,
    val currencyId: Int?,
    val currencyType: String?,
    val createdAt: String?,
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
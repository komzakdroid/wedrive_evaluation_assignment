package com.komzak.wedriveevaluationassignment.domain.model

import com.komzak.wedriveevaluationassignment.data.remote.model.request.CreateBalanceRecordsRequest

data class CreateBalanceRecordData(
    val amount: Int,
    val userId: Int,
    val balanceId: Int,
    val companyId: Int,
    val details: String,
    val currencyId: Int,
    val type: Int  //1- sotish , 2- olish
)

fun CreateBalanceRecordData.toRequest(): CreateBalanceRecordsRequest {
    return CreateBalanceRecordsRequest(
        amount = amount,
        userId = userId,
        balanceId = balanceId,
        companyId = companyId,
        details = details,
        currencyId = currencyId,
        type = type
    )
}
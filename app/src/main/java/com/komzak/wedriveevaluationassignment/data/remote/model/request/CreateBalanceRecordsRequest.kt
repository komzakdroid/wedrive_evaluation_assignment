package com.komzak.wedriveevaluationassignment.data.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*
{
    "amount" : 100,
    "user_id" : 3,
    "balance_id" : 4,
    "company_id" : 1,
    "details" : "sefseffs",
    "currency_id" : 2,
    "type" : 1
}
 */

@Serializable
data class CreateBalanceRecordsRequest(
    val amount: Double,
    @SerialName("user_id") val userId: Int,
    @SerialName("balance_id") val balanceId: Int,
    @SerialName("company_id") val companyId: Int,
    val details: String,
    @SerialName("currency_id") val currencyId: Int,
    val type: Int
)
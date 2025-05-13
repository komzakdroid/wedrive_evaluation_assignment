package com.komzak.wedriveevaluationassignment.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BalanceRecordsResponse(
    val data: List<BalanceRecordsItemResponse>?,
)

@Serializable
data class BalanceRecordsItemResponse(
    val id: Int?,
    val amount: Double?,
    @SerialName("serial_no") val serialNo: String?,
    @SerialName("user_id") val userId: Int?,
    @SerialName("balance_id") val balanceId: Int?,
    val details: String?,
    val type: Int?,
    @SerialName("currency_type") val currencyType: String?,
    @SerialName("currency_id") val currencyId: Int?,
    @SerialName("created_at") val createdAt: String?,
)
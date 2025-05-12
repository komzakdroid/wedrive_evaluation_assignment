package com.komzak.wedriveevaluationassignment.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllBalanceResponse(
    val data: List<BalanceResponse>?,
)

@Serializable
data class BalanceResponse(
    val id: Int?,
    val balance: Double?,
    @SerialName("user_id") val userId: Int?,
    @SerialName("in_out_lay") val inOutLay: Double?,
    @SerialName("out_in_lay") val outInLay: Double?,
    @SerialName("company_id") val companyId: Int?,
    @SerialName("currency_id") val currencyId: Int?,
    @SerialName("currency_type") val currencyType: String?,
    @SerialName("created_at") val createdAt: String?,
    @SerialName("updated_at") val updatedAt: String?
)
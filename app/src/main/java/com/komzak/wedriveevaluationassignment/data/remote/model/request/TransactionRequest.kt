package com.komzak.wedriveevaluationassignment.data.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionRequest(
    val amount: Int,
    @SerialName("service_fee") val serviceFee: Int,
    @SerialName("from_currency_type_id") val fromCurrencyTypeId: Int,
    @SerialName("to_currency_type_id") val toCurrencyTypeId: Int,
    @SerialName("sender_id") val senderId: Int,
    @SerialName("from_city_id") val fromCityId: Int,
    @SerialName("to_city_id") val toCityId: Int,
    @SerialName("receiver_id") val receiverId: Int,
    @SerialName("receiver_name") val receiverName: String,
    @SerialName("receiver_phone") val receiverPhone: String,
    val details: String,
    val type: Int,
    @SerialName("company_id") val companyId: Int,
    @SerialName("balance_id") val balanceId: Int

)
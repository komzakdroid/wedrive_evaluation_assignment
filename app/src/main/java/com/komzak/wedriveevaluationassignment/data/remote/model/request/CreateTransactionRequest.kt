package com.komzak.wedriveevaluationassignment.data.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateTransactionRequest(
    val amount: Int, // user creates
    @SerialName("service_fee") val serviceFee: Int,
    @SerialName("from_currency_type_id") val fromCurrencyTypeId: Int,
    @SerialName("to_currency_type_id") val toCurrencyTypeId: Int,
    @SerialName("sender_id") val senderId: Int,
    @SerialName("from_city_id") val fromCityId: Int, // user selects from cities list
    @SerialName("to_city_id") val toCityId: Int, // user selects from cities list
    @SerialName("receiver_id") val receiverId: Int, // user selects from users list
    @SerialName("receiver_name") val receiverName: String, // user creates
    @SerialName("receiver_phone") val receiverPhone: String, //
    val details: String, // user creates
    @SerialName("company_id") val companyId: Int, // must be get from selected balance's company id
    @SerialName("balance_id") val balanceId: Int // must be get from selected balance's id
)
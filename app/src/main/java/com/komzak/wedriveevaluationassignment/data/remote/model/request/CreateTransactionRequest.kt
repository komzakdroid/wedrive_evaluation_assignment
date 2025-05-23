package com.komzak.wedriveevaluationassignment.data.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*
this is CreateTransactionRequest
{
    "amount": 321,
    "service_fee": 123,
    "from_currency_type_id": 1,
    "to_currency_type_id": 1,
    "sender_id": 1,
    "from_city_id": 2,
    "to_city_id": 3,
    "receiver_id": 2,
    "receiver_name": "soqqaboy",
    "receiver_phone": "204581010",
    "details": "Soqqa qilish uchun olindi",
    "type": 1,
    "company_id": 1,
    "balance_id": 1
}
 */
@Serializable
data class CreateTransactionRequest(
    val amount: Double,
    @SerialName("service_fee") val serviceFee: Double,
    @SerialName("from_currency_type_id") val fromCurrencyTypeId: Int,
    @SerialName("to_currency_type_id") val toCurrencyTypeId: Int,
    @SerialName("sender_id") val senderId: Int,
    @SerialName("from_city_id") val fromCityId: Int,
    @SerialName("to_city_id") val toCityId: Int,
    @SerialName("receiver_id") val receiverId: Int,
    @SerialName("receiver_name") val receiverName: String,
    @SerialName("receiver_phone") val receiverPhone: String,
    val details: String,
    @SerialName("company_id") val companyId: Int,
    @SerialName("balance_id") val balanceId: Int
)
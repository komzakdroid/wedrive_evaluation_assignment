package com.komzak.wedriveevaluationassignment.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionCreateResponse(
    val data: TransactionItemResponse,
)

@Serializable
data class TransactionResponse(
    val data: List<TransactionItemResponse>,
)

@Serializable
data class TransactionItemResponse(
    val id: Long,
    val amount: Long,
    @SerialName("service_fee")
    val serviceFee: Long,
    @SerialName("from_currency_type_id")
    val fromCurrencyTypeId: Long,
    @SerialName("to_currency_type_id")
    val toCurrencyTypeId: Long,
    @SerialName("sender_id")
    val senderId: Long,
    @SerialName("serial_no")
    val serialNo: String,
    @SerialName("receiver_id")
    val receiverId: Long,
    @SerialName("from_city_id")
    val fromCityId: Long,
    @SerialName("to_city_id")
    val toCityId: Long,
    @SerialName("receiver_name")
    val receiverName: String,
    @SerialName("receiver_phone")
    val receiverPhone: String,
    val details: String,
    val type: Long,
    @SerialName("from_currency_type")
    val fromCurrencyType: String,
    @SerialName("to_currency_type")
    val toCurrencyType: String,
    val status: Long,
    @SerialName("company_id")
    val companyId: Long,
    @SerialName("balance_id")
    val balanceId: Long,
    @SerialName("created_at")
    val createdAt: String,
)

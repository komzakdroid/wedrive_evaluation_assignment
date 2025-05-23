package com.komzak.wedriveevaluationassignment.domain.model

import com.komzak.wedriveevaluationassignment.data.remote.model.response.TransactionItemResponse


data class TransactionModel(
    val id: Long? = null,
    val amount: Long? = null,
    val serviceFee: Long? = null,
    val fromCurrencyTypeId: Long? = null,
    val toCurrencyTypeId: Long? = null,
    val senderId: Long? = null,
    val serialNo: String? = null,
    val receiverId: Long? = null,
    val fromCityId: Long? = null,
    val toCityId: Long? = null,
    val receiverName: String? = null,
    val receiverPhone: String? = null,
    val details: String? = null,
    val type: Long? = null,
    val fromCurrencyType: String? = null,
    val toCurrencyType: String? = null,
    val status: Long? = null,
    val companyId: Long? = null,
    val balanceId: Long? = null,
    val createdAt: String? = null
)

fun TransactionItemResponse.toDomain(): TransactionModel {
    return TransactionModel(
        id = id,
        amount = amount,
        serviceFee = serviceFee,
        fromCurrencyTypeId = fromCurrencyTypeId,
        toCurrencyTypeId = toCurrencyTypeId,
        senderId = senderId,
        serialNo = serialNo,
        receiverId = receiverId,
        fromCityId = fromCityId,
        toCityId = toCityId,
        receiverName = receiverName,
        receiverPhone = receiverPhone,
        details = details,
        type = type,
        fromCurrencyType = fromCurrencyType,
        toCurrencyType = toCurrencyType,
        status = status,
        companyId = companyId,
        balanceId = balanceId,
        createdAt = createdAt
    )
}
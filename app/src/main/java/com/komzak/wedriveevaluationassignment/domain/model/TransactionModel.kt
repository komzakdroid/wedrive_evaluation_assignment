package com.komzak.wedriveevaluationassignment.domain.model

import com.komzak.wedriveevaluationassignment.data.remote.model.response.TransactionItemResponse


data class TransactionModel(
    val id: Long,
    val amount: Long,
    val serviceFee: Long,
    val fromCurrencyTypeId: Long,
    val toCurrencyTypeId: Long,
    val senderId: Long,
    val serialNo: String,
    val receiverId: Long,
    val fromCityId: Long,
    val toCityId: Long,
    val receiverName: String,
    val receiverPhone: String,
    val details: String,
    val type: Long,
    val fromCurrencyType: String,
    val toCurrencyType: String,
    val status: Long,
    val companyId: Long,
    val balanceId: Long,
    val createdAt: String,
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
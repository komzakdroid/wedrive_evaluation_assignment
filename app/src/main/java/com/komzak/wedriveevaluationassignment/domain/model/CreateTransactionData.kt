package com.komzak.wedriveevaluationassignment.domain.model

import com.komzak.wedriveevaluationassignment.data.remote.model.request.CreateTransactionRequest

data class CreateTransactionData(
    val amount: Double,
    val serviceFee: Double,
    val fromCurrencyTypeId: Int,
    val toCurrencyTypeId: Int,
    val senderId: Int,
    val fromCityId: Int,
    val toCityId: Int,
    val receiverId: Int,
    val receiverName: String,
    val receiverPhone: String,
    val details: String,
    val companyId: Int,
    val balanceId: Int
)

fun CreateTransactionData.toRequest(): CreateTransactionRequest {
    return CreateTransactionRequest(
        amount = amount,
        serviceFee = serviceFee,
        fromCurrencyTypeId = fromCurrencyTypeId,
        toCurrencyTypeId = toCurrencyTypeId,
        senderId = senderId,
        fromCityId = fromCityId,
        toCityId = toCityId,
        receiverId = receiverId,
        receiverName = receiverName,
        receiverPhone = receiverPhone,
        details = details,
        companyId = companyId,
        balanceId = balanceId
    )
}
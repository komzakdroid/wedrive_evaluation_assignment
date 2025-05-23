package com.komzak.wedriveevaluationassignment.domain.model

import com.komzak.wedriveevaluationassignment.data.remote.model.request.CreateTransactionRequest

data class CreateTransactionData(
    val amount: Int,  // user creates
    val serviceFee: Int, // user creates
    val fromCurrencyTypeId: Int,  // select from selected balance's currency id
    val toCurrencyTypeId: Int,// must be same with from_currency_type_id
    val senderId: Int,  // user selects from users list
    val fromCityId: Int, // user selects from cities list
    val toCityId: Int, // user selects from cities list
    val receiverId: Int, // user selects from users list
    val receiverName: String, // user creates
    val receiverPhone: String, // user creates
    val details: String, // user creates
    val companyId: Int, // must be get from selected balance's company id
    val balanceId: Int // must be get from selected balance's id
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
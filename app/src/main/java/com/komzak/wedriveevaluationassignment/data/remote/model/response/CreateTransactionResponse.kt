package com.komzak.wedriveevaluationassignment.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class CreateTransactionResponse(
    val data: TransactionItemResponse?,
)
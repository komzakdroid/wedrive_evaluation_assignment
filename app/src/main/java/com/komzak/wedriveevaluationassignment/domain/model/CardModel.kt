package com.komzak.wedriveevaluationassignment.domain.model

import com.komzak.wedriveevaluationassignment.data.remote.model.response.CardResponse

data class CardModel(
    val id: Long,
    val number: String,
    val expireDate: String,
    val userId: Long
)

fun CardResponse.toDomain(): CardModel {
    return CardModel(
        id = this.id,
        number = this.number,
        expireDate = this.expireDate,
        userId = this.userId
    )
}
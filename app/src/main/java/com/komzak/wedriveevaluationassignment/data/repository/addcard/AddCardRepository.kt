package com.komzak.wedriveevaluationassignment.data.repository.addcard

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.model.response.CardResponse

interface AddCardRepository {
    suspend fun addCard(number: String,expire:String): DataResult<CardResponse, AppError>
}

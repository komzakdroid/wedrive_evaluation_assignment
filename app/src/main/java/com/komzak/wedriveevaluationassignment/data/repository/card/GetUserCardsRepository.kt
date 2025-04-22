package com.komzak.wedriveevaluationassignment.data.repository.card

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.model.response.CardResponse

interface GetUserCardsRepository {
    suspend fun getUserCards(): DataResult<List<CardResponse>, AppError>
}

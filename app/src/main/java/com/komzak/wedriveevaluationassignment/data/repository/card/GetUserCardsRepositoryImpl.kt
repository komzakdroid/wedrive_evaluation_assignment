package com.komzak.wedriveevaluationassignment.data.repository.card

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.local.DataStoreHelper
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.model.response.CardResponse
import com.komzak.wedriveevaluationassignment.utils.executeApiCall
import kotlinx.coroutines.flow.firstOrNull

class GetUserCardsRepositoryImpl(
    private val api: WeDriveApi,
    private val dataStoreHelper: DataStoreHelper
) : GetUserCardsRepository {
    override suspend fun getUserCards(): DataResult<List<CardResponse>, AppError> {
        val phone = dataStoreHelper.getPhoneNumber().firstOrNull() ?: ""
        return executeApiCall(
            call = { api.getCards(phone) }
        )
    }
}
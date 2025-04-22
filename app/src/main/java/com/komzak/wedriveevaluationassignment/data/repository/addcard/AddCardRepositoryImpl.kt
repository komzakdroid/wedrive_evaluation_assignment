package com.komzak.wedriveevaluationassignment.data.repository.addcard

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.local.DataStoreHelper
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.model.request.CardRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.response.CardResponse
import com.komzak.wedriveevaluationassignment.utils.executeApiCall
import kotlinx.coroutines.flow.firstOrNull

class AddCardRepositoryImpl(
    private val api: WeDriveApi,
    private val dataStoreHelper: DataStoreHelper
) : AddCardRepository {
    override suspend fun addCard(
        number: String,
        expire: String
    ): DataResult<CardResponse, AppError> {
        val phone = dataStoreHelper.getPhoneNumber().firstOrNull() ?: ""

        return executeApiCall(
            call = { api.addCard(CardRequest(number, expire), phone) }
        )
    }
}
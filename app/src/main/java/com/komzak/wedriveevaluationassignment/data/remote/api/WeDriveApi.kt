package com.komzak.wedriveevaluationassignment.data.remote.api

import com.komzak.wedriveevaluationassignment.data.remote.model.request.CardRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.request.MethodRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.request.PromoRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.request.UserRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.response.CardResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.PromoResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserResponse

interface WeDriveApi {
    suspend fun createUser(request: UserRequest): UserResponse
    suspend fun getWallet(phone: String): UserResponse
    suspend fun getCards(phone: String): List<CardResponse>
    suspend fun activatePromo(request: PromoRequest, phone: String): PromoResponse
    suspend fun updateMethod(request: MethodRequest, phone: String): UserResponse
    suspend fun addCard(request: CardRequest, phone: String): CardResponse
}
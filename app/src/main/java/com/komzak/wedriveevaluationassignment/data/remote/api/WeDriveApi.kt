package com.komzak.wedriveevaluationassignment.data.remote.api

import com.komzak.wedriveevaluationassignment.data.remote.model.request.PromoRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.request.UserRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.response.PromoResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserResponse

interface WeDriveApi {
    suspend fun createUser(request: UserRequest): UserResponse
    suspend fun getWallet(phone: String): UserResponse
    suspend fun addPromo(request: PromoRequest): PromoResponse
}
package com.komzak.wedriveevaluationassignment.data.remote.api

import com.komzak.wedriveevaluationassignment.data.remote.model.request.UserLoginRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.request.UserRegisterRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.response.AllBalanceResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.BalanceRecordsResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserLoginResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserRegisterResponse

interface WeDriveApi {
    suspend fun register(request: UserRegisterRequest): UserRegisterResponse
    suspend fun login(request: UserLoginRequest): UserLoginResponse

    suspend fun getAllBalance(): AllBalanceResponse
    suspend fun getBalanceRecordsById(userId:Int): BalanceRecordsResponse
}
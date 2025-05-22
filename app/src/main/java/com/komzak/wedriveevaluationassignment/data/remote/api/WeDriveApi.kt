package com.komzak.wedriveevaluationassignment.data.remote.api

import com.komzak.wedriveevaluationassignment.data.remote.model.request.TransactionRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.request.UserLoginRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.request.UserRegisterRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.response.AllBalanceResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.BalanceRecordsResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.TransactionCreateResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.TransactionResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserLoginResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserRegisterResponse

interface WeDriveApi {
    suspend fun register(request: UserRegisterRequest): UserRegisterResponse
    suspend fun login(request: UserLoginRequest): UserLoginResponse

    suspend fun getAllBalance(): AllBalanceResponse
    suspend fun getAllBalanceById(userId: Int): AllBalanceResponse
    suspend fun getBalanceRecordsById(userId: Int): BalanceRecordsResponse
    suspend fun createTransaction(request: TransactionRequest): TransactionCreateResponse
    suspend fun getTransactionsByBalanceId(balanceId: Int): TransactionResponse
    suspend fun getTransactionsByUserId(userId: Int): TransactionResponse
    suspend fun getTransactionsByStatus(status: String): TransactionResponse
    suspend fun getTransactionsByDate(date: String): TransactionResponse
}

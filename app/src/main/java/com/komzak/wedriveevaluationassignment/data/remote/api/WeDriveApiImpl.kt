package com.komzak.wedriveevaluationassignment.data.remote.api

import com.komzak.wedriveevaluationassignment.data.remote.model.request.CreateBalanceRecordsRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.request.CreateTransactionRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.request.TransactionRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.request.UserLoginRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.request.UserRegisterRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.response.AllBalanceResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.AllCitiesResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.AllUsersResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.BalanceRecordsResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.CompleteActionResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.CreateBalanceRecordsResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.CreateTransactionResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.TransactionCreateResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.TransactionResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserLoginResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserRegisterResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class WeDriveApiImpl(private val client: HttpClient) : WeDriveApi {

    companion object {
        private const val BASE_URL = "https://mubashshir.website/api/v1"
        private const val REGISTER = "$BASE_URL/users/register"
        private const val LOGIN = "$BASE_URL/users/login"
        private const val ALL_BALANCE = "$BASE_URL/user/balances/all"
        private const val ALL_USERS = "$BASE_URL/user/all"
        private const val ALL_CITIES = "$BASE_URL/user/cities/all"
        private const val ALL_BALANCE_BY_ID = "$BASE_URL/user/balances/user/"
        private const val ALL_BALANCE_HISTORIES_BY_ID = "$BASE_URL/user/balances-records/balance/"
        private const val BALANCE_RECORDS_BY_ID = "$BASE_URL/user/balances-records/user/"
        private const val TRANSACTION_CREATE = "$BASE_URL/user/transactions/create"
        private const val TRANSACTIONS_BY_BALANCE_ID = "$BASE_URL/user/transactions/all/balance/"
        private const val TRANSACTIONS_BY_USER_ID = "$BASE_URL/user/transactions/all/user/"
        private const val TRANSACTIONS_BY_RECEIVER_ID = "$BASE_URL/user/transactions/all/receiver/"
        private const val TRANSACTIONS_BY_STATUS = "$BASE_URL/user/transactions/all/active/"
        private const val TRANSACTIONS_BY_DATE = "$BASE_URL/user/transactions/all/date/"
        private const val COMPLETE_ACTION_BY_ID = "$BASE_URL/user/transactions/complete/"
        private const val CREATE_TRANSACTION = "$BASE_URL/user/transactions/create"
        private const val CREATE_BALANCE_RECORDS = "$BASE_URL/user/balances-records"
    }

    override suspend fun register(request: UserRegisterRequest): UserRegisterResponse {
        return client.post(REGISTER) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun login(request: UserLoginRequest): UserLoginResponse {
        return client.post(LOGIN) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun getAllUsers(): AllUsersResponse {
        return client.get(ALL_USERS) {
        }.body()
    }

    override suspend fun getAllCities(): AllCitiesResponse {
        return client.get(ALL_CITIES) {
        }.body()
    }

    override suspend fun getAllBalance(): AllBalanceResponse {
        return client.get(ALL_BALANCE) {
        }.body()
    }

    override suspend fun getAllBalanceById(userId: Int): AllBalanceResponse {
        return client.get("$ALL_BALANCE_BY_ID$userId") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun completeActionById(actionId: String): CompleteActionResponse {
        return client.get("$COMPLETE_ACTION_BY_ID$actionId") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun createBalanceRecords(request: CreateBalanceRecordsRequest): CreateBalanceRecordsResponse {
        return client.post(CREATE_BALANCE_RECORDS) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun createTransaction(request: CreateTransactionRequest): CreateTransactionResponse {
        return client.post(CREATE_TRANSACTION) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun getAllBalanceRecordsById(balanceId: Int): BalanceRecordsResponse {
        return client.get("$ALL_BALANCE_HISTORIES_BY_ID$balanceId") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun getBalanceRecordsById(userId: Int): BalanceRecordsResponse {
        return client.get("$BALANCE_RECORDS_BY_ID$userId") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun createTransaction(request: TransactionRequest): TransactionCreateResponse {
        return client.post(TRANSACTION_CREATE) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun getTransactionsByBalanceId(balanceId: Int): TransactionResponse {
        return client.get("$TRANSACTIONS_BY_BALANCE_ID$balanceId") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun getTransactionsByUserId(userId: Int): TransactionResponse {
        return client.get("$TRANSACTIONS_BY_USER_ID$userId") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun getTransactionsByReceiverId(receiverId: Int): TransactionResponse {
        return client.get("$TRANSACTIONS_BY_RECEIVER_ID$receiverId") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun getTransactionsByStatus(status: String): TransactionResponse {
        return client.get("$TRANSACTIONS_BY_STATUS$status") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun getTransactionsByDate(date: String): TransactionResponse {
        return client.get("$TRANSACTIONS_BY_DATE$date") {
            contentType(ContentType.Application.Json)
        }.body()
    }


    /*
     override suspend fun updateMethod(request: MethodRequest, phone: String): UserResponse {
            return client.put(UPDATE_METHOD_ENDPOINT) {
                contentType(ContentType.Application.Json)
                headers {
                    append("X-Account-Phone", phone)
                }
                setBody(request)
            }.body()
        }*/
}
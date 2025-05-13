package com.komzak.wedriveevaluationassignment.data.remote.api

import com.komzak.wedriveevaluationassignment.data.remote.model.request.UserLoginRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.request.UserRegisterRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.response.AllBalanceResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.BalanceRecordsResponse
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
        private const val BALANCE_RECORDS_BY_ID = "$BASE_URL/user/balances-records/user/"
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

    override suspend fun getAllBalance(): AllBalanceResponse {
        return client.get(ALL_BALANCE) {
        }.body()
    }

    override suspend fun getBalanceRecordsById(userId: Int): BalanceRecordsResponse {
        return client.get("$BALANCE_RECORDS_BY_ID$userId") {
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
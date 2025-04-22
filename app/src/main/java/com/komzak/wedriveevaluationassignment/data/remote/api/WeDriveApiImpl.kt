package com.komzak.wedriveevaluationassignment.data.remote.api

import com.komzak.wedriveevaluationassignment.data.remote.model.request.CardRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.request.MethodRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.request.PromoRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.request.UserRequest
import com.komzak.wedriveevaluationassignment.data.remote.model.response.CardResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.PromoResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class WeDriveApiImpl(private val client: HttpClient) : WeDriveApi {

    companion object {
        private const val BASE_URL = "https://wedrive-assignment-api.onrender.com"
        private const val CREATE_USER_ENDPOINT = "$BASE_URL/users"
        private const val WALLET_ENDPOINT = "$BASE_URL/wallet"
        private const val CARD_ENDPOINT = "$BASE_URL/cards"
        private const val ACTIVATE_PROMO_ENDPOINT = "$BASE_URL/promocode"
        private const val UPDATE_METHOD_ENDPOINT = "$BASE_URL/wallet/method"
    }

    override suspend fun createUser(request: UserRequest): UserResponse {
        return client.post(CREATE_USER_ENDPOINT) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun getWallet(phone: String): UserResponse {
        return client.get(WALLET_ENDPOINT) {
            headers {
                append("X-Account-Phone", phone)
            }
        }.body()
    }

    override suspend fun getCards(phone: String): List<CardResponse> {
        return client.get(CARD_ENDPOINT) {
            headers {
                append("X-Account-Phone", phone)
            }
        }.body()
    }

    override suspend fun addCard(request: CardRequest, phone: String): CardResponse {
        return client.post(CARD_ENDPOINT) {
            contentType(ContentType.Application.Json)
            headers {
                append("X-Account-Phone", phone)
            }
            setBody(request)
        }.body()
    }

    override suspend fun activatePromo(request: PromoRequest, phone: String): PromoResponse {
        return client.post(ACTIVATE_PROMO_ENDPOINT) {
            contentType(ContentType.Application.Json)
            headers {
                append("X-Account-Phone", phone)
            }
            setBody(request)
        }.body()
    }

    override suspend fun updateMethod(request: MethodRequest, phone: String): UserResponse {
        return client.put(UPDATE_METHOD_ENDPOINT) {
            contentType(ContentType.Application.Json)
            headers {
                append("X-Account-Phone", phone)
            }
            setBody(request)
        }.body()
    }
}
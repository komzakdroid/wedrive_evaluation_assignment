package com.komzak.wedriveevaluationassignment.utils

import android.util.Log
import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun <T> executeApiCall(
    call: suspend () -> T
): DataResult<T, AppError> {
    return try {
        val result = call()
        DataResult.Success(result)
    } catch (e: Exception) {
        ApiErrorHandler.handleError(e)
    }
}

object ApiErrorHandler {
    private const val TAG = "ApiErrorHandler"

    suspend fun <T> handleError(exception: Exception): DataResult<T, AppError> {
        return when (exception) {
            is ClientRequestException -> {
                val statusCode = exception.response.status.value
                val errorMessage = parseErrorMessage(exception)
                Log.e(TAG, "Client error: $statusCode, message: $errorMessage", exception)
                DataResult.Error(AppError.ServerError(statusCode, errorMessage))
            }

            is ServerResponseException -> {
                val statusCode = exception.response.status.value
                val errorMessage = parseErrorMessage(exception)
                Log.e(TAG, "Server error: $statusCode, message: $errorMessage", exception)
                DataResult.Error(AppError.ServerError(statusCode, errorMessage))
            }

            else -> {
                Log.e(TAG, "Network error", exception)
                DataResult.Error(AppError.NetworkError)
            }
        }
    }

    private suspend fun parseErrorMessage(exception: Exception): String? {
        return try {
            val responseBody = when (exception) {
                is ClientRequestException -> exception.response.bodyAsText()
                is ServerResponseException -> exception.response.bodyAsText()
                else -> return null
            }

            Log.d(TAG, "Response body: $responseBody") // For debugging

            val json = Json.parseToJsonElement(responseBody).jsonObject

            // Try multiple possible error keys that backends commonly use
            val errorMessage = json["Error"]?.jsonPrimitive?.content
                ?: json["error"]?.jsonPrimitive?.content
                ?: json["message"]?.jsonPrimitive?.content
                ?: json["Message"]?.jsonPrimitive?.content
                ?: json["errorMessage"]?.jsonPrimitive?.content
                ?: json["detail"]?.jsonPrimitive?.content

            errorMessage?.ifBlank { null }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse error message", e)
            null
        }
    }
}
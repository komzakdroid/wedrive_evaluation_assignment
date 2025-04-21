package com.komzak.wedriveevaluationassignment.common

sealed class AppError {
    object NetworkError : AppError()
    data class ServerError(val statusCode: Int, val message: String? = null) : AppError()
}
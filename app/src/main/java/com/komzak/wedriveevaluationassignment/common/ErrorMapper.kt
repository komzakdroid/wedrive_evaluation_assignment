package com.komzak.wedriveevaluationassignment.common

import com.komzak.wedriveevaluationassignment.R

fun mapStatusCodeToErrorType(statusCode: Int): HttpErrorType {
    return when (statusCode) {
        400 -> HttpErrorType.ClientError.BadRequest
        401 -> HttpErrorType.ClientError.Unauthorized
        403 -> HttpErrorType.ClientError.Forbidden
        404 -> HttpErrorType.ClientError.NotFound
        in 400..499 -> HttpErrorType.ClientError.Generic

        500 -> HttpErrorType.ServerError.InternalServerError
        503 -> HttpErrorType.ServerError.ServiceUnavailable
        in 500..599 -> HttpErrorType.ServerError.Generic

        else -> HttpErrorType.ServerError.Generic
    }
}

fun <S> mapErrorToMessage(error: AppError, resource: ResourceProvider): DataResult<S, String> {
    val errorMessage = when (error) {
        AppError.NetworkError -> resource.getString(R.string.network_error)
        is AppError.ServerError -> {
            error.message?.takeIf { it.isNotBlank() }
                ?: resource.getString(mapStatusCodeToErrorType(error.statusCode).stringResId)
        }
    }
    return DataResult.Error(errorMessage)
}
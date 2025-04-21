package com.komzak.wedriveevaluationassignment.common

import com.komzak.wedriveevaluationassignment.R

sealed class HttpErrorType(val stringResId: Int) {
    sealed class ClientError(stringResId: Int) : HttpErrorType(stringResId) {
        object BadRequest : ClientError(R.string.bad_request) // 400
        object Unauthorized : ClientError(R.string.unauthorized) // 401
        object Forbidden : ClientError(R.string.forbidden) // 403
        object NotFound : ClientError(R.string.not_found) // 404
        object Generic : ClientError(R.string.client_error) // Other 4xx
    }

    sealed class ServerError(stringResId: Int) : HttpErrorType(stringResId) {
        object InternalServerError : ServerError(R.string.server_error) // 500
        object ServiceUnavailable : ServerError(R.string.service_unavailable) // 503
        object Generic : ServerError(R.string.server_error) // Other 5xx
    }
}
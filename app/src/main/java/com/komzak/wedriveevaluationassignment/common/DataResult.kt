package com.komzak.wedriveevaluationassignment.common

sealed class DataResult<S, E> {
    data class Success<S, E>(val data: S) : DataResult<S, E>()
    data class Error<S, E>(val error: E) : DataResult<S, E>()
}
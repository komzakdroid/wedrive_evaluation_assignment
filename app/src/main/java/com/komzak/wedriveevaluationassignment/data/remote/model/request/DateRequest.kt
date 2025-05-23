package com.komzak.wedriveevaluationassignment.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class DateRequest(
    val from: String,
    val to: String
)
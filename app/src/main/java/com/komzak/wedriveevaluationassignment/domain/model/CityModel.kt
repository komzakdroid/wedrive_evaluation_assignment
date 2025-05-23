package com.komzak.wedriveevaluationassignment.domain.model

import com.komzak.wedriveevaluationassignment.data.remote.model.response.CityResponse

fun CityResponse.toDomain() = CityModel(
    id = id ?: 0,
    name = name ?: "",
    parentId = parentId,
    companyId = companyId,
    createdAt = createdAt ?: "",
)

data class CityModel(
    val id: Int,
    val name: String,
    val parentId: Int?,
    val companyId: Int?,
    val createdAt: String,
)
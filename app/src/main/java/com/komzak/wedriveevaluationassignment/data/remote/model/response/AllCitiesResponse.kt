package com.komzak.wedriveevaluationassignment.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllCitiesResponse(
    val data: List<CityResponse>?,
)

@Serializable
data class CityResponse(
    val id: Int?,
    val name: String?,
    @SerialName("parent_id") val parentId: Int?,
    @SerialName("company_id") val companyId: Int?,
    @SerialName("created_at") val createdAt: String?,
)
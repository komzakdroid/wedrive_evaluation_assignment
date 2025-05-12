package com.komzak.wedriveevaluationassignment.domain.model

import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserLoginResponse
import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserRegisterResponse


data class UserModelData(
    val id: Int? = null,
    val username: String? = null,
    val phone: String? = null,
    val role: Int? = null,
    val createdAt: String? = null,
    val avatar: String? = null,
    val token: String? = null
)

fun UserLoginResponse.toDomain(): UserModelData {
    return UserModelData(
        id = data?.user?.id,
        username = data?.user?.username,
        phone = data?.user?.phone,
        role = data?.user?.role,
        createdAt = data?.user?.createdAt,
        avatar = data?.user?.avatar,
        token = data?.token
    )
}

fun UserRegisterResponse.toDomain(): UserModelData {
    return UserModelData(
        id = data?.id,
        username = data?.username,
        phone = data?.phone,
        role = data?.role,
        createdAt = data?.createdAt,
        avatar = data?.avatar
    )
}
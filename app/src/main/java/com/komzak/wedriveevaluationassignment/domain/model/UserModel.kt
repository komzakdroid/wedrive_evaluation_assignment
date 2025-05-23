package com.komzak.wedriveevaluationassignment.domain.model

import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserResponse

data class UserModel(
    val id: Int,
    val username: String,
    val phone: String,
    val role: Int,
    val password: String,
    val avatar: String? = null,
    val createdAt: String,
)

fun UserResponse.toDomain() = UserModel(
    id = id ?: 0,
    username = username ?: "",
    phone = phone ?: "",
    role = role ?: 0,
    password = password ?: "",
    avatar = avatar,
    createdAt = createdAt ?: "",
)
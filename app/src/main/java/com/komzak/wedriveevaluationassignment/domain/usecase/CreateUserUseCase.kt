package com.komzak.wedriveevaluationassignment.domain.usecase

import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.common.DispatchersProvider
import com.komzak.wedriveevaluationassignment.common.ResourceProvider
import com.komzak.wedriveevaluationassignment.common.mapErrorToMessage
import com.komzak.wedriveevaluationassignment.data.repository.user.register.CreateUserRepository
import com.komzak.wedriveevaluationassignment.domain.model.UserModelData
import com.komzak.wedriveevaluationassignment.domain.model.toDomain
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
class CreateUserUseCase(
    private val repository: CreateUserRepository,
    private val dispatcher: DispatchersProvider,
    private val resource: ResourceProvider
) {
    suspend operator fun invoke(
        username: String,
        phone: String,
        role: Int,
        password: String
    ): DataResult<UserModelData, String> {
        return withContext(dispatcher.io) {
            when (val result = repository.register(
                username = username,
                phone = phone,
                role = role,
                password = password
            )) {
                is DataResult.Success -> DataResult.Success(result.data.toDomain())
                is DataResult.Error -> mapErrorToMessage(result.error, resource)
            }
        }
    }
}
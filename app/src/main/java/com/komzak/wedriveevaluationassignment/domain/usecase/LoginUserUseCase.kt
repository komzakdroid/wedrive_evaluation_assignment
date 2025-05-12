package com.komzak.wedriveevaluationassignment.domain.usecase

import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.common.DispatchersProvider
import com.komzak.wedriveevaluationassignment.common.ResourceProvider
import com.komzak.wedriveevaluationassignment.common.mapErrorToMessage
import com.komzak.wedriveevaluationassignment.data.local.DataStoreHelper
import com.komzak.wedriveevaluationassignment.data.repository.user.login.LoginUserRepository
import com.komzak.wedriveevaluationassignment.domain.model.UserModelData
import com.komzak.wedriveevaluationassignment.domain.model.toDomain
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
class LoginUserUseCase(
    private val repository: LoginUserRepository,
    private val dispatcher: DispatchersProvider,
    private val dataStoreHelper: DataStoreHelper,
    private val resource: ResourceProvider
) {
    suspend operator fun invoke(
        phone: String,
        password: String
    ): DataResult<UserModelData, String> {
        return withContext(dispatcher.io) {
            when (val result = repository.login(phone, password)) {
                is DataResult.Success -> {
                    result.data.data?.token?.let { dataStoreHelper.saveToken(it) }
                    result.data.data?.user?.phone?.let { dataStoreHelper.savePhoneNumber(it) }
                    DataResult.Success(result.data.toDomain())
                }

                is DataResult.Error -> mapErrorToMessage(result.error, resource)
            }
        }
    }
}
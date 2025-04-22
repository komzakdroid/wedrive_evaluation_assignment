package com.komzak.wedriveevaluationassignment.domain.usecase

import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.common.DispatchersProvider
import com.komzak.wedriveevaluationassignment.common.ResourceProvider
import com.komzak.wedriveevaluationassignment.common.mapErrorToMessage
import com.komzak.wedriveevaluationassignment.data.repository.method.UpdateMethodRepository
import com.komzak.wedriveevaluationassignment.domain.model.PaymentMethod
import com.komzak.wedriveevaluationassignment.domain.model.UserModel
import com.komzak.wedriveevaluationassignment.domain.model.toDomain
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
class UpdateMethodUseCase(
    private val repository: UpdateMethodRepository,
    private val dispatcher: DispatchersProvider,
    private val resource: ResourceProvider
) {
    suspend operator fun invoke(
        payment: PaymentMethod,
        cardId: Long?
    ): DataResult<UserModel, String> {
        return withContext(dispatcher.io) {
            when (val result = repository.updateMethod(payment, cardId)) {
                is DataResult.Success -> DataResult.Success(result.data.toDomain())
                is DataResult.Error -> mapErrorToMessage(result.error, resource)
            }
        }
    }
}
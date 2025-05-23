package com.komzak.wedriveevaluationassignment.domain.usecase

import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.common.DispatchersProvider
import com.komzak.wedriveevaluationassignment.common.ResourceProvider
import com.komzak.wedriveevaluationassignment.common.mapErrorToMessage
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.CompleteActionByIdRepository
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
class CompleteActionByIdUseCase(
    private val repository: CompleteActionByIdRepository,
    private val dispatcher: DispatchersProvider,
    private val resource: ResourceProvider
) {
    suspend operator fun invoke(
        actionId: String
    ): DataResult<String, String> {
        return withContext(dispatcher.io) {
            when (val result = repository.completeAction(actionId)) {
                is DataResult.Success -> DataResult.Success(result.data.data ?: "Completed")

                is DataResult.Error -> mapErrorToMessage(result.error, resource)
            }
        }
    }
}
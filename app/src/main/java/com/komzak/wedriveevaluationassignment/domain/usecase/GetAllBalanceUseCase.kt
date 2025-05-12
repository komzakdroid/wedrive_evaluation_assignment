package com.komzak.wedriveevaluationassignment.domain.usecase

import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.common.DispatchersProvider
import com.komzak.wedriveevaluationassignment.common.ResourceProvider
import com.komzak.wedriveevaluationassignment.common.mapErrorToMessage
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.GetAllBalanceRepository
import com.komzak.wedriveevaluationassignment.domain.model.BalanceModel
import com.komzak.wedriveevaluationassignment.domain.model.toDomain
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
class GetAllBalanceUseCase(
    private val repository: GetAllBalanceRepository,
    private val dispatcher: DispatchersProvider,
    private val resource: ResourceProvider
) {
    suspend operator fun invoke(): DataResult<List<BalanceModel>, String> {
        return withContext(dispatcher.io) {
            when (val result = repository.getAllBalance()) {
                is DataResult.Success -> {
                    DataResult.Success(result.data.data?.map { it.toDomain() } ?: emptyList())
                }

                is DataResult.Error -> mapErrorToMessage(result.error, resource)
            }
        }
    }
}
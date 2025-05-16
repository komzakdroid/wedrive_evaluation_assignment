package com.komzak.wedriveevaluationassignment.domain.usecase

import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.common.DispatchersProvider
import com.komzak.wedriveevaluationassignment.common.ResourceProvider
import com.komzak.wedriveevaluationassignment.common.mapErrorToMessage
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.TransactionsByStatusRepository
import com.komzak.wedriveevaluationassignment.domain.model.TransactionModel
import com.komzak.wedriveevaluationassignment.domain.model.toDomain
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
class GetTransactionsByStatusUseCase(
    private val repository: TransactionsByStatusRepository,
    private val dispatcher: DispatchersProvider,
    private val resource: ResourceProvider
) {
    suspend operator fun invoke(
        status: String
    ): DataResult<List<TransactionModel>, String> {
        return withContext(dispatcher.io) {
            when (val result = repository.getTransactionsByStatus(status)) {
                is DataResult.Success -> DataResult.Success(result.data.data.map { it.toDomain() })

                is DataResult.Error -> mapErrorToMessage(result.error, resource)
            }
        }
    }
}
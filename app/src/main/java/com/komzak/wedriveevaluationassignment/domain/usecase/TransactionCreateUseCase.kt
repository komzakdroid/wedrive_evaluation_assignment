package com.komzak.wedriveevaluationassignment.domain.usecase

import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.common.DispatchersProvider
import com.komzak.wedriveevaluationassignment.common.ResourceProvider
import com.komzak.wedriveevaluationassignment.common.mapErrorToMessage
import com.komzak.wedriveevaluationassignment.data.remote.model.request.TransactionRequest
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.TransactionCreateRepository
import com.komzak.wedriveevaluationassignment.domain.model.TransactionModel
import com.komzak.wedriveevaluationassignment.domain.model.toDomain
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
class TransactionCreateUseCase(
    private val repository: TransactionCreateRepository,
    private val dispatcher: DispatchersProvider,
    private val resource: ResourceProvider
) {
    suspend operator fun invoke(
        request: TransactionRequest
    ): DataResult<TransactionModel, String> {
        return withContext(dispatcher.io) {
            when (val result = repository.createTransaction(
                request
            )) {
                is DataResult.Success -> DataResult.Success(result.data.data.toDomain())
                is DataResult.Error -> mapErrorToMessage(result.error, resource)
            }
        }
    }
}
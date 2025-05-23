package com.komzak.wedriveevaluationassignment.domain.usecase

import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.common.DispatchersProvider
import com.komzak.wedriveevaluationassignment.common.ResourceProvider
import com.komzak.wedriveevaluationassignment.common.mapErrorToMessage
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.CreateTransactionRepository
import com.komzak.wedriveevaluationassignment.domain.model.CreateTransactionData
import com.komzak.wedriveevaluationassignment.domain.model.TransactionModel
import com.komzak.wedriveevaluationassignment.domain.model.toDomain
import com.komzak.wedriveevaluationassignment.domain.model.toRequest
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
class CreateTransactionUseCase(
    private val repository: CreateTransactionRepository,
    private val dispatcher: DispatchersProvider,
    private val resource: ResourceProvider
) {
    suspend operator fun invoke(
        data: CreateTransactionData
    ): DataResult<TransactionModel, String> {
        return withContext(dispatcher.io) {
            when (val result = repository.createTransaction(data.toRequest())) {
                is DataResult.Success -> DataResult.Success(
                    result.data.data?.toDomain() ?: TransactionModel()
                )

                is DataResult.Error -> mapErrorToMessage(result.error, resource)
            }
        }
    }
}
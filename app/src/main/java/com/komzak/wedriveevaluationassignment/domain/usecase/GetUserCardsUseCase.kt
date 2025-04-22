package com.komzak.wedriveevaluationassignment.domain.usecase

import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.common.DispatchersProvider
import com.komzak.wedriveevaluationassignment.common.ResourceProvider
import com.komzak.wedriveevaluationassignment.common.mapErrorToMessage
import com.komzak.wedriveevaluationassignment.data.repository.card.GetUserCardsRepository
import com.komzak.wedriveevaluationassignment.domain.model.CardModel
import com.komzak.wedriveevaluationassignment.domain.model.toDomain
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
class GetUserCardsUseCase(
    private val repository: GetUserCardsRepository,
    private val dispatcher: DispatchersProvider,
    private val resource: ResourceProvider
) {
    suspend operator fun invoke(): DataResult<List<CardModel>, String> {
        return withContext(dispatcher.io) {
            when (val result = repository.getUserCards()) {
                is DataResult.Success -> DataResult.Success(result.data.map { it.toDomain() })
                is DataResult.Error -> mapErrorToMessage(result.error, resource)
            }
        }
    }
}
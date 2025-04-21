package com.komzak.wedriveevaluationassignment.domain.usecase

import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.common.DispatchersProvider
import com.komzak.wedriveevaluationassignment.common.ResourceProvider
import com.komzak.wedriveevaluationassignment.common.mapErrorToMessage
import com.komzak.wedriveevaluationassignment.data.repository.promo.AddPromoRepository
import com.komzak.wedriveevaluationassignment.domain.model.PromoModel
import com.komzak.wedriveevaluationassignment.domain.model.toDomain
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
class AddingPromoUseCase(
    private val repository: AddPromoRepository,
    private val dispatcher: DispatchersProvider,
    private val resource: ResourceProvider
) {
    suspend operator fun invoke(code: String): DataResult<PromoModel, String> {
        return withContext(dispatcher.io) {
            when (val result = repository.addPromo(code)) {
                is DataResult.Success -> DataResult.Success(result.data.toDomain())
                is DataResult.Error -> mapErrorToMessage(result.error, resource)
            }
        }
    }
}
package com.komzak.wedriveevaluationassignment.domain.usecase

import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.common.DispatchersProvider
import com.komzak.wedriveevaluationassignment.common.ResourceProvider
import com.komzak.wedriveevaluationassignment.common.mapErrorToMessage
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.GetAllBalanceRecordsByIdRepository
import com.komzak.wedriveevaluationassignment.domain.model.BalanceRecordModel
import com.komzak.wedriveevaluationassignment.domain.model.toDomain
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
class GetAllBalanceRecordsByIdUseCase(
    private val repository: GetAllBalanceRecordsByIdRepository,
    private val dispatcher: DispatchersProvider,
    private val resource: ResourceProvider
) {
    suspend operator fun invoke(balanceId: Int): DataResult<List<BalanceRecordModel>, String> {
        return withContext(dispatcher.io) {
            when (val result = repository.getBalanceRecordsById(balanceId)) {
                is DataResult.Success -> {
                    DataResult.Success(result.data.data?.map { it.toDomain() } ?: emptyList())
                }

                is DataResult.Error -> mapErrorToMessage(result.error, resource)
            }
        }
    }
}
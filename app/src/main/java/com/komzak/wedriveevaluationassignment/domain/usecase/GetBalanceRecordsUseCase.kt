package com.komzak.wedriveevaluationassignment.domain.usecase

import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.common.DispatchersProvider
import com.komzak.wedriveevaluationassignment.common.ResourceProvider
import com.komzak.wedriveevaluationassignment.common.mapErrorToMessage
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.GetBalanceRecordsRepository
import com.komzak.wedriveevaluationassignment.domain.model.BalanceRecordModel
import com.komzak.wedriveevaluationassignment.domain.model.toDomain
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
class GetBalanceRecordsUseCase(
    private val repository: GetBalanceRecordsRepository,
    private val dispatcher: DispatchersProvider,
    private val resource: ResourceProvider
) {
    suspend operator fun invoke(uid: String): DataResult<List<BalanceRecordModel>, String> {
        return withContext(dispatcher.io) {
            when (val result = repository.getBalanceRecords(uid.toInt())) {
                is DataResult.Success -> {
                    DataResult.Success(result.data.data?.map { it.toDomain() } ?: emptyList())
                }

                is DataResult.Error -> mapErrorToMessage(result.error, resource)
            }
        }
    }
}
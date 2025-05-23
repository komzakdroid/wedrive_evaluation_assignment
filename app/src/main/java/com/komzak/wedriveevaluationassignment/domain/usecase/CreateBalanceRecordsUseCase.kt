package com.komzak.wedriveevaluationassignment.domain.usecase

import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.common.DispatchersProvider
import com.komzak.wedriveevaluationassignment.common.ResourceProvider
import com.komzak.wedriveevaluationassignment.common.mapErrorToMessage
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.CreateBalanceRecordsRepository
import com.komzak.wedriveevaluationassignment.domain.model.BalanceRecordModel
import com.komzak.wedriveevaluationassignment.domain.model.CreateBalanceRecordData
import com.komzak.wedriveevaluationassignment.domain.model.toDomain
import com.komzak.wedriveevaluationassignment.domain.model.toRequest
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
class CreateBalanceRecordsUseCase(
    private val repository: CreateBalanceRecordsRepository,
    private val dispatcher: DispatchersProvider,
    private val resource: ResourceProvider
) {
    suspend operator fun invoke(
        data: CreateBalanceRecordData
    ): DataResult<BalanceRecordModel, String> {
        return withContext(dispatcher.io) {
            when (val result = repository.createRecord(data.toRequest())) {
                is DataResult.Success -> DataResult.Success(
                    result.data.data?.toDomain() ?: BalanceRecordModel()
                )

                is DataResult.Error -> mapErrorToMessage(result.error, resource)
            }
        }
    }
}
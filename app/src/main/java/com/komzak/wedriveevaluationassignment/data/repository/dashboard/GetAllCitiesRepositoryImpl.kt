package com.komzak.wedriveevaluationassignment.data.repository.dashboard

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.model.response.AllCitiesResponse
import com.komzak.wedriveevaluationassignment.utils.executeApiCall

class GetAllCitiesRepositoryImpl(private val api: WeDriveApi) : GetAllCitiesRepository {
    override suspend fun getAllCities(): DataResult<AllCitiesResponse, AppError> {
        return executeApiCall(
            call = {
                api.getAllCities()
            }
        )
    }
}
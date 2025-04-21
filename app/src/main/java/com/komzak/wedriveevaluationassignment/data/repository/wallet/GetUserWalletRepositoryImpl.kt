package com.komzak.wedriveevaluationassignment.data.repository.wallet

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserResponse
import com.komzak.wedriveevaluationassignment.utils.executeApiCall

class GetUserWalletRepositoryImpl(private val api: WeDriveApi) : GetUserWalletRepository {
    override suspend fun getUserWallet(phone: String): DataResult<UserResponse, AppError> {
        return executeApiCall(
            call = { api.getWallet(phone) }
        )
    }
}
package com.komzak.wedriveevaluationassignment.data.repository.wallet

import com.komzak.wedriveevaluationassignment.common.AppError
import com.komzak.wedriveevaluationassignment.common.DataResult
import com.komzak.wedriveevaluationassignment.data.remote.model.response.UserResponse

interface GetUserWalletRepository {
    suspend fun getUserWallet(phone: String): DataResult<UserResponse, AppError>
}

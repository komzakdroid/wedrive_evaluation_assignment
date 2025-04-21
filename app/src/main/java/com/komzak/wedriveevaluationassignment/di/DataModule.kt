package com.komzak.wedriveevaluationassignment.di

import com.komzak.wedriveevaluationassignment.data.remote.KtorClient
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApiImpl
import com.komzak.wedriveevaluationassignment.data.repository.promo.AddPromoRepository
import com.komzak.wedriveevaluationassignment.data.repository.promo.AddPromoRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.user.CreateUserRepository
import com.komzak.wedriveevaluationassignment.data.repository.user.CreateUserRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.wallet.GetUserWalletRepository
import com.komzak.wedriveevaluationassignment.data.repository.wallet.GetUserWalletRepositoryImpl
import com.komzak.wedriveevaluationassignment.domain.usecase.CreateUserUseCase
import com.komzak.wedriveevaluationassignment.presentation.ui.auth.CreateUserVM
import com.komzak.wedriveevaluationassignment.presentation.ui.wallet.WalletViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single { KtorClient.httpClient }
    singleOf(::WeDriveApiImpl) { bind<WeDriveApi>() }
    singleOf(::CreateUserRepositoryImpl) { bind<CreateUserRepository>() }
    singleOf(::GetUserWalletRepositoryImpl) { bind<GetUserWalletRepository>() }
    singleOf(::AddPromoRepositoryImpl) { bind<AddPromoRepository>() }
}

val domainModule = module {
    factoryOf(::CreateUserUseCase)
}

val presentationModule = module {
    viewModel { CreateUserVM(get()) }
    viewModel { WalletViewModel() }
}
package com.komzak.wedriveevaluationassignment.di

import com.komzak.wedriveevaluationassignment.data.local.DataStoreHelper
import com.komzak.wedriveevaluationassignment.data.remote.KtorClient
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApiImpl
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.GetAllBalanceRepository
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.GetAllBalanceRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.user.login.LoginUserRepository
import com.komzak.wedriveevaluationassignment.data.repository.user.login.LoginUserRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.user.register.CreateUserRepository
import com.komzak.wedriveevaluationassignment.data.repository.user.register.CreateUserRepositoryImpl
import com.komzak.wedriveevaluationassignment.domain.usecase.CreateUserUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetAllBalanceUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.LoginUserUseCase
import com.komzak.wedriveevaluationassignment.presentation.ui.home.HomeViewModel
import com.komzak.wedriveevaluationassignment.presentation.ui.login.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single { DataStoreHelper(androidContext()) }
    single { KtorClient.createHttpClient(get(), get()) }
    singleOf(::WeDriveApiImpl) { bind<WeDriveApi>() }
    singleOf(::CreateUserRepositoryImpl) { bind<CreateUserRepository>() }
    singleOf(::LoginUserRepositoryImpl) { bind<LoginUserRepository>() }
    singleOf(::GetAllBalanceRepositoryImpl) { bind<GetAllBalanceRepository>() }
}

val domainModule = module {
    factoryOf(::CreateUserUseCase)
    factoryOf(::LoginUserUseCase)
    factoryOf(::GetAllBalanceUseCase)
}

val presentationModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
}

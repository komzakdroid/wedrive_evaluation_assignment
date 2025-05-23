package com.komzak.wedriveevaluationassignment.di

import com.komzak.wedriveevaluationassignment.data.local.DataStoreHelper
import com.komzak.wedriveevaluationassignment.data.remote.KtorClient
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApiImpl
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.CompleteActionByIdRepository
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.CompleteActionByIdRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.GetAllBalanceByIdRepository
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.GetAllBalanceByIdRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.GetAllBalanceRecordsByIdRepository
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.GetAllBalanceRecordsByIdRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.GetAllBalanceRepository
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.GetAllBalanceRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.GetAllCitiesRepository
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.GetAllCitiesRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.GetAllUsersRepository
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.GetAllUsersRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.GetBalanceRecordsRepository
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.GetBalanceRecordsRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.TransactionCreateRepository
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.TransactionCreateRequestRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.TransactionsByBalanceIdRepository
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.TransactionsByBalanceIdRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.TransactionsByDateRepository
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.TransactionsByDateRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.TransactionsByReceiverIdRepository
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.TransactionsByReceiverIdRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.TransactionsByStatusRepository
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.TransactionsByStatusRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.TransactionsByUserIdRepository
import com.komzak.wedriveevaluationassignment.data.repository.dashboard.TransactionsByUserIdRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.user.login.LoginUserRepository
import com.komzak.wedriveevaluationassignment.data.repository.user.login.LoginUserRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.user.register.CreateUserRepository
import com.komzak.wedriveevaluationassignment.data.repository.user.register.CreateUserRepositoryImpl
import com.komzak.wedriveevaluationassignment.domain.usecase.CompleteActionByIdUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.CreateUserUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetAllBalanceByIdUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetAllBalanceRecordsByIdUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetAllBalanceUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetAllCitiesUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetAllUsersUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetBalanceRecordsUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetTransactionsByBalanceIdUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetTransactionsByDateUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetTransactionsByReceiverIdUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetTransactionsByStatusUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetTransactionsByUserIdUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.LoginUserUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.TransactionCreateUseCase
import com.komzak.wedriveevaluationassignment.presentation.ui.history.HistoryViewModel
import com.komzak.wedriveevaluationassignment.presentation.ui.home.HomeViewModel
import com.komzak.wedriveevaluationassignment.presentation.ui.login.LoginViewModel
import com.komzak.wedriveevaluationassignment.presentation.ui.orders.OrdersHistoryViewModel
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
    singleOf(::GetBalanceRecordsRepositoryImpl) { bind<GetBalanceRecordsRepository>() }
    singleOf(::TransactionCreateRequestRepositoryImpl) { bind<TransactionCreateRepository>() }
    singleOf(::TransactionsByBalanceIdRepositoryImpl) { bind<TransactionsByBalanceIdRepository>() }
    singleOf(::TransactionsByDateRepositoryImpl) { bind<TransactionsByDateRepository>() }
    singleOf(::TransactionsByStatusRepositoryImpl) { bind<TransactionsByStatusRepository>() }
    singleOf(::TransactionsByUserIdRepositoryImpl) { bind<TransactionsByUserIdRepository>() }
    singleOf(::GetAllBalanceByIdRepositoryImpl) { bind<GetAllBalanceByIdRepository>() }
    singleOf(::GetAllBalanceByIdRepositoryImpl) { bind<GetAllBalanceByIdRepository>() }
    singleOf(::GetAllBalanceRecordsByIdRepositoryImpl) { bind<GetAllBalanceRecordsByIdRepository>() }
    singleOf(::TransactionsByReceiverIdRepositoryImpl) { bind<TransactionsByReceiverIdRepository>() }
    singleOf(::GetAllUsersRepositoryImpl) { bind<GetAllUsersRepository>() }
    singleOf(::GetAllCitiesRepositoryImpl) { bind<GetAllCitiesRepository>() }
    singleOf(::CompleteActionByIdRepositoryImpl) { bind<CompleteActionByIdRepository>() }
}

val domainModule = module {
    factoryOf(::CreateUserUseCase)
    factoryOf(::LoginUserUseCase)
    factoryOf(::GetAllBalanceUseCase)
    factoryOf(::GetBalanceRecordsUseCase)
    factoryOf(::TransactionCreateUseCase)
    factoryOf(::GetTransactionsByBalanceIdUseCase)
    factoryOf(::GetTransactionsByDateUseCase)
    factoryOf(::GetTransactionsByStatusUseCase)
    factoryOf(::GetTransactionsByUserIdUseCase)
    factoryOf(::GetAllBalanceByIdUseCase)
    factoryOf(::GetAllBalanceRecordsByIdUseCase)
    factoryOf(::GetTransactionsByReceiverIdUseCase)
    factoryOf(::GetAllUsersUseCase)
    factoryOf(::GetAllCitiesUseCase)
    factoryOf(::GetAllBalanceByIdUseCase)
    factoryOf(::CompleteActionByIdUseCase)
}

val presentationModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get(), get(), get(), get()) }
    viewModel { HistoryViewModel(get(), get()) }
    viewModel {
        OrdersHistoryViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}

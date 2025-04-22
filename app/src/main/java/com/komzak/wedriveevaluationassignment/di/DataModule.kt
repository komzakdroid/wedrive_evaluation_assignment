package com.komzak.wedriveevaluationassignment.di

import com.komzak.wedriveevaluationassignment.data.remote.KtorClient
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApi
import com.komzak.wedriveevaluationassignment.data.remote.api.WeDriveApiImpl
import com.komzak.wedriveevaluationassignment.data.repository.addcard.AddCardRepository
import com.komzak.wedriveevaluationassignment.data.repository.addcard.AddCardRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.card.GetUserCardsRepository
import com.komzak.wedriveevaluationassignment.data.repository.card.GetUserCardsRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.method.UpdateMethodRepository
import com.komzak.wedriveevaluationassignment.data.repository.method.UpdateMethodRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.promo.ActivatePromoRepository
import com.komzak.wedriveevaluationassignment.data.repository.promo.ActivatePromoRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.user.CreateUserRepository
import com.komzak.wedriveevaluationassignment.data.repository.user.CreateUserRepositoryImpl
import com.komzak.wedriveevaluationassignment.data.repository.wallet.GetUserWalletRepository
import com.komzak.wedriveevaluationassignment.data.repository.wallet.GetUserWalletRepositoryImpl
import com.komzak.wedriveevaluationassignment.domain.usecase.ActivatePromoUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.AddCardUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.CreateUserUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetUserCardsUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.GetUserWalletUseCase
import com.komzak.wedriveevaluationassignment.domain.usecase.UpdateMethodUseCase
import com.komzak.wedriveevaluationassignment.presentation.ui.addcard.AddCardViewModel
import com.komzak.wedriveevaluationassignment.presentation.ui.wallet.WalletViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single { KtorClient.createHttpClient(get()) }
    singleOf(::WeDriveApiImpl) { bind<WeDriveApi>() }
    singleOf(::CreateUserRepositoryImpl) { bind<CreateUserRepository>() }
    singleOf(::GetUserWalletRepositoryImpl) { bind<GetUserWalletRepository>() }
    singleOf(::ActivatePromoRepositoryImpl) { bind<ActivatePromoRepository>() }
    singleOf(::UpdateMethodRepositoryImpl) { bind<UpdateMethodRepository>() }
    singleOf(::GetUserCardsRepositoryImpl) { bind<GetUserCardsRepository>() }
    singleOf(::AddCardRepositoryImpl) { bind<AddCardRepository>() }
}

val domainModule = module {
    factoryOf(::CreateUserUseCase)
    factoryOf(::GetUserWalletUseCase)
    factoryOf(::ActivatePromoUseCase)
    factoryOf(::UpdateMethodUseCase)
    factoryOf(::GetUserCardsUseCase)
    factoryOf(::AddCardUseCase)
}

val presentationModule = module {
    viewModel { WalletViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { AddCardViewModel(get(), get()) }
}
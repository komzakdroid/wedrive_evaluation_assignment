package com.komzak.wedriveevaluationassignment.di

import com.komzak.wedriveevaluationassignment.common.DispatchersProvider
import com.komzak.wedriveevaluationassignment.common.ResourceProvider
import com.komzak.wedriveevaluationassignment.data.local.DataStoreHelper
import com.komzak.wedriveevaluationassignment.utils.NetworkMonitor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val appModule = module {
    single { ResourceProvider(androidContext()) }
    single { NetworkMonitor(androidContext()) }
    factoryOf(::DispatchersProvider)
}
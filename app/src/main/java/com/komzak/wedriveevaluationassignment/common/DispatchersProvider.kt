package com.komzak.wedriveevaluationassignment.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Factory

@Factory
open class DispatchersProvider {
    open val main: CoroutineDispatcher
        get() = Dispatchers.Main
    open val mainImmediate: CoroutineDispatcher
        get() = Dispatchers.Main.immediate
    open val io: CoroutineDispatcher
        get() = Dispatchers.IO
    open val default: CoroutineDispatcher
        get() = Dispatchers.Default
    open val unconfined: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}
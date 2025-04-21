package com.komzak.wedriveevaluationassignment

import android.app.Application
import com.komzak.wedriveevaluationassignment.di.appModule
import com.komzak.wedriveevaluationassignment.di.dataModule
import com.komzak.wedriveevaluationassignment.di.domainModule
import com.komzak.wedriveevaluationassignment.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(appModule, dataModule, domainModule, presentationModule)
        }
    }
}
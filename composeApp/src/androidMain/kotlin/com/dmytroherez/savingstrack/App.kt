package com.dmytroherez.savingstrack

import android.app.Application
import com.dmytroherez.savingstrack.core.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        multiplatform.network.cmptoast.AppContext.apply { set(applicationContext) }
        initKoin {
            androidLogger()
            androidContext(this@App)
        }
    }
}
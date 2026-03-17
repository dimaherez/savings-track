package com.dmytroherez.savingstrack.core.di

import com.dmytroherez.savingstrack.core.datastore.createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformDataStoreModule: Module = module {
    single { createDataStore() }
}
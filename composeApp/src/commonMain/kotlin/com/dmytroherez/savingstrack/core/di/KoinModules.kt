package com.dmytroherez.savingstrack.core.di

import com.dmytroherez.savingstrack.presentation.auth.AuthViewModel
import com.dmytroherez.savingstrack.presentation.crypto.CryptoViewModel
import com.dmytroherez.savingstrack.presentation.fiat.FiatViewModel
import com.dmytroherez.savingstrack.presentation.home.HomeViewModel
import com.dmytroherez.savingstrack.presentation.income.IncomeViewModel
import com.dmytroherez.savingstrack.core.datastore.DataStoreRepo
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val platformDataStoreModule: Module

val dataModule = module {
    single { DataStoreRepo(dataStore = get()) }
}

val domainModule = module {

}

val presentationModule = module {
    viewModel { AuthViewModel(dataStoreRepo = get()) }
    factoryOf(::HomeViewModel)
    factoryOf(::FiatViewModel)
    factoryOf(::CryptoViewModel)
    factoryOf(::IncomeViewModel)
}

fun appModule() = listOf(platformDataStoreModule, dataModule, domainModule, presentationModule)
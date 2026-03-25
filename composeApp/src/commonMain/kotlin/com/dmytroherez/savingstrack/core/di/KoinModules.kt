package com.dmytroherez.savingstrack.core.di

import com.dmytroherez.savingstrack.MainViewModel
import com.dmytroherez.savingstrack.presentation.auth.AuthViewModel
import com.dmytroherez.savingstrack.presentation.savings.SavingsViewModel
import com.dmytroherez.savingstrack.presentation.home.HomeViewModel
import com.dmytroherez.savingstrack.presentation.income.IncomeViewModel
import com.dmytroherez.savingstrack.core.datastore.DataStoreRepo
import com.dmytroherez.savingstrack.data.repoimpl.AuthRepoImpl
import com.dmytroherez.savingstrack.domain.repo.AuthRepo
import com.dmytroherez.savingstrack.domain.usecase.GetCurrentUserUC
import com.dmytroherez.savingstrack.domain.usecase.LoginUC
import com.dmytroherez.savingstrack.domain.usecase.RegisterUC
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val platformDataStoreModule: Module

val dataModule = module {
    single { DataStoreRepo(dataStore = get()) }

    single<AuthRepo> { AuthRepoImpl() }
}

val domainModule = module {
    single<RegisterUC> { RegisterUC(authRepo = get()) }
    single<LoginUC> { LoginUC(authRepo = get()) }
    single<GetCurrentUserUC> { GetCurrentUserUC(authRepo = get()) }
}

val presentationModule = module {
    viewModel { AuthViewModel(
        dataStoreRepo = get(),
        registerUC = get(),
        loginUC = get()
    ) }

    viewModel { MainViewModel(
        getCurrentUserUC = get()
    ) }

    factoryOf(::HomeViewModel)
    factoryOf(::SavingsViewModel)
    factoryOf(::IncomeViewModel)
}

fun appModule() = listOf(platformDataStoreModule, dataModule, domainModule, presentationModule)
package com.dmytroherez.savingstrack.core.di

import com.dmytroherez.savingstrack.MainViewModel
import com.dmytroherez.savingstrack.presentation.auth.AuthViewModel
import com.dmytroherez.savingstrack.presentation.savings.SavingsViewModel
import com.dmytroherez.savingstrack.presentation.home.HomeViewModel
import com.dmytroherez.savingstrack.presentation.income.IncomeViewModel
import com.dmytroherez.savingstrack.core.datastore.DataStoreRepo
import com.dmytroherez.savingstrack.data.repoimpl.AuthRepoImpl
import com.dmytroherez.savingstrack.data.repoimpl.SavingsRepoImpl
import com.dmytroherez.savingstrack.domain.repo.AuthRepo
import com.dmytroherez.savingstrack.domain.repo.SavingsRepo
import com.dmytroherez.savingstrack.domain.usecase.auth.GetCurrentUserUC
import com.dmytroherez.savingstrack.domain.usecase.auth.LoginUC
import com.dmytroherez.savingstrack.domain.usecase.auth.RegisterUC
import com.dmytroherez.savingstrack.domain.usecase.savings.GetSavingsUC
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val platformDataStoreModule: Module

val dataModule = module {
    single { DataStoreRepo(dataStore = get()) }

    single<AuthRepo> { AuthRepoImpl() }
    single<SavingsRepo> { SavingsRepoImpl() }
}

val domainModule = module {
    single<RegisterUC> { RegisterUC(authRepo = get()) }
    single<LoginUC> { LoginUC(authRepo = get()) }
    single<GetCurrentUserUC> { GetCurrentUserUC(authRepo = get()) }
    single<GetSavingsUC> { GetSavingsUC(savingsRepo = get()) }
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

    viewModel { SavingsViewModel(
        getSavingsUC = get()
    ) }

    factoryOf(::HomeViewModel)
    factoryOf(::IncomeViewModel)
}

fun appModule() = listOf(platformDataStoreModule, dataModule, domainModule, presentationModule)
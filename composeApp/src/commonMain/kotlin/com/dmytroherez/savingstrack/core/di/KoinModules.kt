package com.dmytroherez.savingstrack.core.di

import com.dmytroherez.savingstrack.MainViewModel
import com.dmytroherez.savingstrack.presentation.auth.AuthViewModel
import com.dmytroherez.savingstrack.presentation.savings.SavingsViewModel
import com.dmytroherez.savingstrack.presentation.home.HomeViewModel
import com.dmytroherez.savingstrack.presentation.income.IncomeViewModel
import com.dmytroherez.savingstrack.core.datastore.DataStoreRepo
import com.dmytroherez.savingstrack.core.network.createHttpClient
import com.dmytroherez.savingstrack.data.repoimpl.AuthRepoImpl
import com.dmytroherez.savingstrack.data.repoimpl.SavingsRepoImpl
import com.dmytroherez.savingstrack.domain.repo.AuthRepo
import com.dmytroherez.savingstrack.domain.repo.SavingsRepo
import com.dmytroherez.savingstrack.domain.usecase.auth.GetCurrentUserUC
import com.dmytroherez.savingstrack.domain.usecase.auth.LoginUC
import com.dmytroherez.savingstrack.domain.usecase.auth.RegisterUC
import com.dmytroherez.savingstrack.domain.usecase.savings.GetSavingsUC
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformDataStoreModule: Module

val dataModule = module {
    single { DataStoreRepo(dataStore = get()) }

    single { createHttpClient(
        baseUrl = "http://192.168.0.96:8080", // "http://10.10.10.110:8080"
        firebaseAuth = Firebase.auth
    ) }

    single<AuthRepo> { AuthRepoImpl(firebaseAuth = Firebase.auth) }
    single<SavingsRepo> { SavingsRepoImpl(httpClient = get()) }
}

val domainModule = module {
    singleOf(::RegisterUC)
    singleOf(::LoginUC)
    singleOf(::GetCurrentUserUC)
    singleOf(::GetSavingsUC)
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

    viewModelOf(::HomeViewModel)
    viewModelOf(::IncomeViewModel)
}

fun appModule() = listOf(platformDataStoreModule, dataModule, domainModule, presentationModule)
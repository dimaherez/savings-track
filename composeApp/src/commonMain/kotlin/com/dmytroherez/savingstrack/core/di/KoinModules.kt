package com.dmytroherez.savingstrack.core.di

import com.dmytroherez.savingstrack.auth.presentation.auth.AuthViewModel
import com.dmytroherez.savingstrack.home.HomeViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {

}

val domainModule = module {

}

val presentationModule = module {
    factoryOf(::AuthViewModel)
    factoryOf(::HomeViewModel)
}

fun appModule() = listOf(dataModule, domainModule, presentationModule)
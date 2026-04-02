package com.dmytroherez.savingstrack.server.di

import com.dmytroherez.savingstrack.server.data.repo.GoalsRepoImpl
import com.dmytroherez.savingstrack.server.data.repo.TransactionsRepoImpl
import com.dmytroherez.savingstrack.server.domain.repo.GoalsRepo
import com.dmytroherez.savingstrack.server.domain.repo.TransactionsRepo
import org.koin.dsl.module

val appModule = module {
    single<TransactionsRepo> { TransactionsRepoImpl() }
    single<GoalsRepo> { GoalsRepoImpl() }
}
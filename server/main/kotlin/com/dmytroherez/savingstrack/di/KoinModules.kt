package com.dmytroherez.savingstrack.di

import com.dmytroherez.savingstrack.data.repo.GoalsRepoImpl
import com.dmytroherez.savingstrack.data.repo.TransactionsRepoImpl
import com.dmytroherez.savingstrack.domain.repo.GoalsRepo
import com.dmytroherez.savingstrack.domain.repo.TransactionsRepo
import org.koin.dsl.module

val appModule = module {
    single<TransactionsRepo> { TransactionsRepoImpl() }
    single<GoalsRepo> { GoalsRepoImpl() }
}
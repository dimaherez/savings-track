package com.dmytroherez.savingstrack.core.di

import com.dmytroherez.savingstrack.MainViewModel
import com.dmytroherez.savingstrack.core.datastore.DataStoreRepo
import com.dmytroherez.savingstrack.core.network.createHttpClient
import com.dmytroherez.savingstrack.data.repoimpl.AuthRepoImpl
import com.dmytroherez.savingstrack.data.repoimpl.GoalsRepoImpl
import com.dmytroherez.savingstrack.data.repoimpl.SavingsRepoImpl
import com.dmytroherez.savingstrack.domain.repo.AuthRepo
import com.dmytroherez.savingstrack.domain.repo.GoalsRepo
import com.dmytroherez.savingstrack.domain.repo.SavingsRepo
import com.dmytroherez.savingstrack.domain.usecase.auth.GetCurrentUserUC
import com.dmytroherez.savingstrack.domain.usecase.auth.LoginUC
import com.dmytroherez.savingstrack.domain.usecase.auth.RegisterUC
import com.dmytroherez.savingstrack.domain.usecase.goals.AddGoalUC
import com.dmytroherez.savingstrack.domain.usecase.goals.CompleteGoalUC
import com.dmytroherez.savingstrack.domain.usecase.goals.GetAvailableGoalsUC
import com.dmytroherez.savingstrack.domain.usecase.goals.GetGoalsUC
import com.dmytroherez.savingstrack.domain.usecase.goals.GoalsRefreshTriggerUC
import com.dmytroherez.savingstrack.domain.usecase.savings.GetSavingsDashboardUC
import com.dmytroherez.savingstrack.domain.usecase.savings.GetTransactionsByCurrencyUC
import com.dmytroherez.savingstrack.domain.usecase.savings.PostSavingUC
import com.dmytroherez.savingstrack.domain.usecase.savings.TransactionsRefreshTriggerUC
import com.dmytroherez.savingstrack.presentation.auth.AuthViewModel
import com.dmytroherez.savingstrack.presentation.home.HomeViewModel
import com.dmytroherez.savingstrack.presentation.home.addgoal.AddGoalViewModel
import com.dmytroherez.savingstrack.presentation.income.IncomeViewModel
import com.dmytroherez.savingstrack.presentation.savings.SavingsViewModel
import com.dmytroherez.savingstrack.presentation.savings.addtransaction.AddTransactionViewModel
import com.dmytroherez.savingstrack.presentation.transactions.TransactionsViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformDataStoreModule: Module

val networkModule = module {
    single { Firebase.auth }

    single {
        createHttpClient(
            baseUrl = "http://192.168.0.96:8080", // Consider moving this to BuildConfig later!
            firebaseAuth = get()
        )
    }
}

val dataModule = module {
    singleOf(::DataStoreRepo)
    singleOf(::AuthRepoImpl) bind AuthRepo::class
    singleOf(::SavingsRepoImpl) bind SavingsRepo::class
    singleOf(::GoalsRepoImpl) bind GoalsRepo::class
}

val domainModule = module {
    // Auth
    singleOf(::RegisterUC)
    singleOf(::LoginUC)
    singleOf(::GetCurrentUserUC)

    // Transactions (savings)
    singleOf(::GetTransactionsByCurrencyUC)
    singleOf(::PostSavingUC)
    singleOf(::GetSavingsDashboardUC)
    singleOf(::TransactionsRefreshTriggerUC)

    // Goals
    singleOf(::AddGoalUC)
    singleOf(::GetGoalsUC)
    singleOf(::GetAvailableGoalsUC)
    singleOf(::CompleteGoalUC)
    singleOf(::GoalsRefreshTriggerUC)
}

val presentationModule = module {
    // MainApp
    viewModelOf(::MainViewModel)

    //Auth
    viewModelOf(::AuthViewModel)

    // Transactions/savings
    viewModelOf(::SavingsViewModel)
    viewModelOf(::AddTransactionViewModel)
    viewModelOf(::TransactionsViewModel)

    // Home/goals
    viewModelOf(::HomeViewModel)
    viewModelOf(::AddGoalViewModel)
    viewModelOf(::IncomeViewModel)
}

fun appModule() = listOf(
    platformDataStoreModule,
    networkModule,
    dataModule,
    domainModule,
    presentationModule
)
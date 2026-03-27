package com.dmytroherez.savingstrack.presentation.transactions

sealed interface TransactionsAction {
    data object SomeAction : TransactionsAction
}
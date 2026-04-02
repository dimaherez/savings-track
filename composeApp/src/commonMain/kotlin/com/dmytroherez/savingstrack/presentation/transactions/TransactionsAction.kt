package com.dmytroherez.savingstrack.presentation.transactions

import com.dmytroherez.savingstrack.core.presentation.BaseAction

sealed interface TransactionsAction : BaseAction {
    data object SomeAction : TransactionsAction
}
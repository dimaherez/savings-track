package com.dmytroherez.savingstrack.presentation.income

sealed interface IncomeAction {
    data object SomeAction : IncomeAction
}

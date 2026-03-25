package com.dmytroherez.savingstrack.presentation.savings

sealed interface SavingsAction {
    data object SomeAction : SavingsAction
}

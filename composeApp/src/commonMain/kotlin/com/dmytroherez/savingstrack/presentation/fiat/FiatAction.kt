package com.dmytroherez.savingstrack.presentation.fiat

sealed interface FiatAction {
    data object SomeAction : FiatAction
}

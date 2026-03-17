package com.dmytroherez.savingstrack.auth.presentation.home

sealed interface HomeAction {
    data object SomeAction : HomeAction
}
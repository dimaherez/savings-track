package com.dmytroherez.savingstrack.presentation.home

sealed interface HomeAction {
    data object SomeAction : HomeAction
}
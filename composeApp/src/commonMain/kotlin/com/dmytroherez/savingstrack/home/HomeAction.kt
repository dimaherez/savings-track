package com.dmytroherez.savingstrack.home

sealed interface HomeAction {
    data object SomeAction : HomeAction
}
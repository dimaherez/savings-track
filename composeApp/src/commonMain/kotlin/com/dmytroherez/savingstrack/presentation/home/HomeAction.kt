package com.dmytroherez.savingstrack.presentation.home

sealed interface HomeAction {
    data class CompleteGoal(val goalId: Int) : HomeAction
}
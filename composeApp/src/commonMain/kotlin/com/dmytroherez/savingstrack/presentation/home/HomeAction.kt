package com.dmytroherez.savingstrack.presentation.home

import com.dmytroherez.savingstrack.core.presentation.BaseAction

sealed interface HomeAction : BaseAction {
    data class CompleteGoal(val goalId: Int) : HomeAction
}
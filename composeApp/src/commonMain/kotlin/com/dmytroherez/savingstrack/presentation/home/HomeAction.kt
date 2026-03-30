package com.dmytroherez.savingstrack.presentation.home

import com.dmytroherez.savingstrack.dto.goals.CreateGoalRequest

sealed interface HomeAction {
    data object ToggleAddGoalDialog : HomeAction
    data class AddGoal(val request: CreateGoalRequest) : HomeAction
}
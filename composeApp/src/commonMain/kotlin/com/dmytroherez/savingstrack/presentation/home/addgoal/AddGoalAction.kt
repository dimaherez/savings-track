package com.dmytroherez.savingstrack.presentation.home.addgoal

import com.dmytroherez.savingstrack.core.presentation.BaseAction

sealed interface AddGoalAction : BaseAction {
    data class OnTitleChange(val value: String) : AddGoalAction
    data class OnAmountChange(val value: String) : AddGoalAction
    data class OnCurrencyChange(val value: String) : AddGoalAction
    data class OnDateChange(val value: Long?) : AddGoalAction
    data object ToggleDatePicker : AddGoalAction

    data object AddGoal : AddGoalAction
    data object Dismiss : AddGoalAction
}
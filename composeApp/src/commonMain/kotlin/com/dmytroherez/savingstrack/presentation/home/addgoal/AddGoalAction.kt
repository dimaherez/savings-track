package com.dmytroherez.savingstrack.presentation.home.addgoal

sealed interface AddGoalAction {
    data class OnTitleChange(val value: String) : AddGoalAction
    data class OnAmountChange(val value: String) : AddGoalAction
    data class OnCurrencyChange(val value: String) : AddGoalAction
    data class OnDateChange(val value: Long?) : AddGoalAction
    data object ToggleDatePicker : AddGoalAction

    data object AddGoal : AddGoalAction
    data object Dismiss : AddGoalAction
}
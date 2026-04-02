package com.dmytroherez.savingstrack.presentation.home.addgoal

import com.dmytroherez.savingstrack.core.presentation.BaseUiEvent
import com.dmytroherez.savingstrack.core.presentation.UiText

sealed interface AddGoalEvent : BaseUiEvent {
    data class ShowToast(val message: UiText) : AddGoalEvent
    data object Dismiss : AddGoalEvent
}
package com.dmytroherez.savingstrack.presentation.home

import com.dmytroherez.savingstrack.core.presentation.BaseUiEvent
import com.dmytroherez.savingstrack.core.presentation.UiText

sealed interface HomeEvent: BaseUiEvent {
    data class ShowToast(val message: UiText) : HomeEvent
}
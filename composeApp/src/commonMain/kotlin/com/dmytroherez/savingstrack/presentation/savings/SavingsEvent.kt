package com.dmytroherez.savingstrack.presentation.savings

import com.dmytroherez.savingstrack.core.presentation.BaseUiEvent
import com.dmytroherez.savingstrack.core.presentation.UiText

sealed interface SavingsEvent : BaseUiEvent {
    data class ShowErrorToast(val message: UiText) : SavingsEvent
}

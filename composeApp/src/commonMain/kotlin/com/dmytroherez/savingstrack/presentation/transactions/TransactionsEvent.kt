package com.dmytroherez.savingstrack.presentation.transactions

import com.dmytroherez.savingstrack.core.presentation.BaseUiEvent
import com.dmytroherez.savingstrack.core.presentation.UiText

sealed interface TransactionsEvent : BaseUiEvent {
    data class ShowToast(val message: UiText) : TransactionsEvent
}
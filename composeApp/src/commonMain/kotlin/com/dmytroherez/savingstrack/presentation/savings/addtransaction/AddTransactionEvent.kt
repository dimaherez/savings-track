package com.dmytroherez.savingstrack.presentation.savings.addtransaction

import com.dmytroherez.savingstrack.core.presentation.BaseUiEvent
import com.dmytroherez.savingstrack.core.presentation.UiText

sealed interface AddTransactionEvent : BaseUiEvent {
    data class ShowToast(val message: UiText) : AddTransactionEvent
    data object CloseBottomSheet : AddTransactionEvent
}
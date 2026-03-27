package com.dmytroherez.savingstrack.presentation.savings

import com.dmytroherez.savingstrack.dto.savings.PostTransactionRequest

sealed interface SavingsAction {
    data class PostSaving(val saving: PostTransactionRequest) : SavingsAction
    data object ToggleAddSavingDialog : SavingsAction
}

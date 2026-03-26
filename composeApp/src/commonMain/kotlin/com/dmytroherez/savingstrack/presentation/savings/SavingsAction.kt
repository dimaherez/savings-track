package com.dmytroherez.savingstrack.presentation.savings

import com.dmytroherez.savingstrack.dto.savings.PostSavingRequest

sealed interface SavingsAction {
    data class PostSaving(val saving: PostSavingRequest) : SavingsAction
    data object ToggleAddSavingDialog : SavingsAction
}

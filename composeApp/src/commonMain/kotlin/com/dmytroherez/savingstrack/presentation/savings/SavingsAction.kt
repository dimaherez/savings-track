package com.dmytroherez.savingstrack.presentation.savings

import com.dmytroherez.savingstrack.dto.transactions.SavingCategory

sealed interface SavingsAction {
    data class PostSaving(
        val currency: String,
        val amountInMinorUnits: Long,
        val category: SavingCategory,
        val description: String? = null,
        val goalId: Int? = null
    ) : SavingsAction
    data object ToggleAddSavingDialog : SavingsAction
}

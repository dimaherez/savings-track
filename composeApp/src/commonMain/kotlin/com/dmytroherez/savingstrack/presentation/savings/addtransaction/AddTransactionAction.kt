package com.dmytroherez.savingstrack.presentation.savings.addtransaction

import com.dmytroherez.savingstrack.core.presentation.BaseAction
import com.dmytroherez.savingstrack.shared.dto.goals.GoalForTransactionItem
import com.dmytroherez.savingstrack.shared.dto.transactions.SavingCategory

sealed interface AddTransactionAction : BaseAction {
    data class OnCurrencyChange(val value: String) : AddTransactionAction
    data class OnAmountChange(val value: String) : AddTransactionAction
    data class OnCategoryChange(val value: SavingCategory) : AddTransactionAction
    data class OnDescriptionChange(val value: String) : AddTransactionAction
    data class OnSetGoal(val goal: GoalForTransactionItem?) : AddTransactionAction

    data object OnSaveClick : AddTransactionAction
    data object OnCancelClick : AddTransactionAction
}
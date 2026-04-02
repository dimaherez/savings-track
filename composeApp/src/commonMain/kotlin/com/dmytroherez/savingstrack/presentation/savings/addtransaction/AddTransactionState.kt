package com.dmytroherez.savingstrack.presentation.savings.addtransaction

import com.dmytroherez.savingstrack.core.presentation.BaseUiState
import com.dmytroherez.savingstrack.dto.goals.GoalForTransactionItem
import com.dmytroherez.savingstrack.dto.transactions.SavingCategory

data class AddTransactionState(
    val availableGoals: List<GoalForTransactionItem> = emptyList(),
    val availableCurrencies: List<String> = listOf("USD", "EUR", "UAH", "BTC"),
    val currency: String = "USD",
    val amount: String = "",
    val category: SavingCategory = SavingCategory.FIAT,
    val description: String = "",
    val goal: GoalForTransactionItem? = null,
    val isLoading: Boolean = false
) : BaseUiState

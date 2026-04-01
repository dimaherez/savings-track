package com.dmytroherez.savingstrack.dto.transactions

import com.dmytroherez.savingstrack.dto.goals.GoalForTransactionItem
import kotlinx.serialization.Serializable

@Serializable
data class DashboardResponse(
    val categories: Map<SavingCategory, List<CurrencyTotal>>,
    val availableGoals: List<GoalForTransactionItem>
)

@Serializable
data class CurrencyTotal(
    val currency: String,
    val totalAmount: Long,
    val recentTransactions: List<TransactionItem>,
    val hasMoreTransactions: Boolean
)

package com.dmytroherez.savingstrack.shared.dto.transactions

import kotlinx.serialization.Serializable

@Serializable
data class DashboardResponse(
    val categories: Map<SavingCategory, List<CurrencyTotal>>
)

@Serializable
data class CurrencyTotal(
    val currency: String,
    val totalAmount: Long,
    val recentTransactions: List<TransactionItem>,
    val hasMoreTransactions: Boolean
)

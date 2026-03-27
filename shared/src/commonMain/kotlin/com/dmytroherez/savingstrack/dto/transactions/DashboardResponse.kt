package com.dmytroherez.savingstrack.dto.transactions

import kotlinx.serialization.Serializable

@Serializable
data class DashboardResponse(
    val categories: Map<SavingCategory, List<CurrencyTotal>>
)

@Serializable
data class CurrencyTotal(
    val currency: String,
    val totalAmount: Double,
    val recentTransactions: List<TransactionItem>,
    val hasMoreTransactions: Boolean
)

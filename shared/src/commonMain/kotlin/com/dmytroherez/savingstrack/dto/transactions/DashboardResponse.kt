package com.dmytroherez.savingstrack.dto.transactions

import kotlinx.serialization.Serializable

@Serializable
data class DashboardResponse(
    val categories: Map<SavingCategory, CategorySummary>
)

@Serializable
data class CategorySummary(
    val currencyTotals: List<CurrencyTotal>,
    val recentTransactions: List<TransactionItem>
)

@Serializable
data class CurrencyTotal(
    val currency: String,
    val totalAmount: Double
)

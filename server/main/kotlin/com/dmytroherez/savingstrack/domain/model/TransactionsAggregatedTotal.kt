package com.dmytroherez.savingstrack.domain.model

import com.dmytroherez.savingstrack.dto.transactions.SavingCategory

data class TransactionsAggregatedTotal(
    val category: SavingCategory,
    val currency: String,
    val sum: Double
)

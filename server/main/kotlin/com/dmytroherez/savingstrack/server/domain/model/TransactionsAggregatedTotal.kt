package com.dmytroherez.savingstrack.server.domain.model

import com.dmytroherez.savingstrack.shared.dto.transactions.SavingCategory

data class TransactionsAggregatedTotal(
    val category: SavingCategory,
    val currency: String,
    val sum: Long
)

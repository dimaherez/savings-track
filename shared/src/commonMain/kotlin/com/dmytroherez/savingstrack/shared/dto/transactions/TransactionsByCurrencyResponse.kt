package com.dmytroherez.savingstrack.shared.dto.transactions

import kotlinx.serialization.Serializable

@Serializable
data class TransactionsByCurrencyResponse(
    val totalAmount: Long,
    val transactions: List<TransactionItem>
)

package com.dmytroherez.savingstrack.dto.transactions

import kotlinx.serialization.Serializable

@Serializable
data class TransactionsByCurrencyResponse(
    val totalAmount: Long,
    val transactions: List<TransactionItem>
)

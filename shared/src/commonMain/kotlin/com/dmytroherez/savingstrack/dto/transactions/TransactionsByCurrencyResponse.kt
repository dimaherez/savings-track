package com.dmytroherez.savingstrack.dto.transactions

import kotlinx.serialization.Serializable

@Serializable
data class TransactionsByCurrencyResponse(
    val totalAmount: ULong,
    val transactions: List<TransactionItem>
)

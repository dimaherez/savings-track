package com.dmytroherez.savingstrack.dto.transactions

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class TransactionItem(
    val id: Int,
    val userId: String,
    val currency: String,
    val amount: Long,
    val description: String? = null,
    val category: SavingCategory,
    val createdAt: Instant
)

package com.dmytroherez.savingstrack.dto.savings

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class TransactionItem(
    val id: Int,
    val userId: String,
    val currency: String,
    val amount: Double,
    val description: String? = null,
    val category: SavingCategory,
    val createdAt: Instant
)

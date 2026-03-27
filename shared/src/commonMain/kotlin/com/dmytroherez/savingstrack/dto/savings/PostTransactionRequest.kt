package com.dmytroherez.savingstrack.dto.savings

import kotlinx.serialization.Serializable

@Serializable
data class PostTransactionRequest(
    val currency: String,
    val amount: Double,
    val description: String? = null,
    val category: SavingCategory
)

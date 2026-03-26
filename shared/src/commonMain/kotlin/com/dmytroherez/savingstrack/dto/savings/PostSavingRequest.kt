package com.dmytroherez.savingstrack.dto.savings

import kotlinx.serialization.Serializable

@Serializable
data class PostSavingRequest(
    val currency: String,
    val amount: Double,
    val description: String? = null
)

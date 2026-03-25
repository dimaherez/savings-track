package org.example.dto.savings

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class GetSavingsResponseItem @OptIn(ExperimentalTime::class) constructor(
    val id: Int,
    val userId: String,
    val currency: String,
    val amount: Double,
    val description: String? = null,
    val createdAt: Instant
)

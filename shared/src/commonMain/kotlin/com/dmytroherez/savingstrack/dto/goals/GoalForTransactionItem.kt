package com.dmytroherez.savingstrack.dto.goals

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class GoalForTransactionItem(
    val id: Int,
    val title: String,
    val lastTransaction: Instant?,
    val progress: Float,
    val targetAmount: Long,
    val currentAmount: Long
)

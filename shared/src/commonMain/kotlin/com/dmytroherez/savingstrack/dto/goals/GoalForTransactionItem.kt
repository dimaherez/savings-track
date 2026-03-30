package com.dmytroherez.savingstrack.dto.goals

import kotlinx.serialization.Serializable

@Serializable
data class GoalForTransactionItem(
    val id: Int,
    val title: String
)

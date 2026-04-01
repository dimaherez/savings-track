package com.dmytroherez.savingstrack.dto.goals

import com.dmytroherez.savingstrack.dto.transactions.TransactionItem
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class GoalItem(
    val id: Int,
    val title: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val currency: String,
    val progressPercentage: Int,
    val createdAt: Instant,
    val completedAt: Instant?,
    val deadline: LocalDate? = null,
    val recentTransactions: List<TransactionItem> = emptyList()
)
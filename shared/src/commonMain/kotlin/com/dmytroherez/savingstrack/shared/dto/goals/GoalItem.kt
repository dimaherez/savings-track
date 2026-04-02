package com.dmytroherez.savingstrack.shared.dto.goals

import com.dmytroherez.savingstrack.shared.dto.transactions.TransactionItem
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class GoalItem(
    val id: Int,
    val title: String,
    val targetAmount: Long,
    val currentAmount: Long,
    val currency: String,
    val progress: Float,
    val createdAt: Instant,
    val completedAt: Instant?,
    val deadline: LocalDate?,
    val recentTransactions: List<TransactionItem>,
    val hasMoreTransactions: Boolean
)
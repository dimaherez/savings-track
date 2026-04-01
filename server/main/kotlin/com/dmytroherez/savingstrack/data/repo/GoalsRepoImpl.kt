package com.dmytroherez.savingstrack.data.repo

import com.dmytroherez.savingstrack.data.tables.GoalsTable
import com.dmytroherez.savingstrack.data.tables.GoalsTable.createdAt
import com.dmytroherez.savingstrack.data.tables.GoalsTable.currency
import com.dmytroherez.savingstrack.data.tables.GoalsTable.deadline
import com.dmytroherez.savingstrack.data.tables.GoalsTable.id
import com.dmytroherez.savingstrack.data.tables.GoalsTable.targetAmount
import com.dmytroherez.savingstrack.data.tables.GoalsTable.title
import com.dmytroherez.savingstrack.data.tables.TransactionsTable
import com.dmytroherez.savingstrack.dbQuery
import com.dmytroherez.savingstrack.domain.repo.GoalsRepo
import com.dmytroherez.savingstrack.dto.goals.CreateGoalRequest
import com.dmytroherez.savingstrack.dto.goals.GoalItem
import com.dmytroherez.savingstrack.dto.transactions.TransactionItem
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.leftJoin
import org.jetbrains.exposed.v1.core.sum
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import kotlin.math.roundToInt
import kotlin.time.Clock

class GoalsRepoImpl: GoalsRepo {
    override suspend fun addGoal(userId: String, request: CreateGoalRequest) {
        dbQuery {
            GoalsTable
                .insert {
                    it[firebaseUid] = userId
                    it[title] = request.title
                    it[targetAmount] = request.targetAmount
                    it[currency] = request.currency
                    it[deadline] = request.deadline
                }[id]
        }
    }

    override suspend fun getGoals(userId: String): List<GoalItem> {
        return dbQuery {
            val currentAmountSum = TransactionsTable.amount.sum()

            val goals = GoalsTable.leftJoin(TransactionsTable, { id }, { TransactionsTable.goalId })
                .select(
                    id,
                    title,
                    targetAmount,
                    currency,
                    deadline,
                    createdAt,
                    currentAmountSum
                )
                .where { GoalsTable.firebaseUid eq userId }
                .groupBy(
                    id,
                    title,
                    targetAmount,
                    currency,
                    deadline,
                    createdAt
                )
                .map { row ->
                    val targetAmount = row[targetAmount]

                    val currentAmount = row[currentAmountSum] ?: 0.0

                    val progressPercentage = if (targetAmount > 0.0) {
                        ((currentAmount / targetAmount) * 100).roundToInt()
                    } else {
                        0
                    }

                    GoalItem(
                        id = row[id],
                        title = row[title],
                        targetAmount = targetAmount,
                        currentAmount = currentAmount,
                        currency = row[currency],
                        progressPercentage = progressPercentage,
                        deadline = row[deadline],
                        createdAt = row[createdAt]
                    )
                }

            goals.map { goal ->
                val lastTransactions = TransactionsTable
                    .selectAll()
                    .where { TransactionsTable.goalId eq goal.id }
                    .orderBy(TransactionsTable.createdAt to SortOrder.DESC)
                    .limit(3)
                    .map { row ->
                        TransactionItem(
                            id = row[TransactionsTable.id],
                            userId = row[TransactionsTable.firebaseUid],
                            category = row[TransactionsTable.category],
                            currency = row[TransactionsTable.currency],
                            amount = row[TransactionsTable.amount],
                            description = row[TransactionsTable.description],
                            createdAt = row[TransactionsTable.createdAt]
                        )
                    }

                goal.copy(recentTransactions = lastTransactions)
            }
        }
    }

    override suspend fun setAsCompleted(goalId: Int) {
        dbQuery {
            GoalsTable.update(where = { id eq goalId }) {
                it[completedAt] = Clock.System.now()
            }
        }
    }
}
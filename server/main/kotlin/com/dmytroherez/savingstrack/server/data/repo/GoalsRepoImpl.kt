package com.dmytroherez.savingstrack.server.data.repo

import com.dmytroherez.savingstrack.server.data.tables.GoalsTable
import com.dmytroherez.savingstrack.server.data.tables.GoalsTable.completedAt
import com.dmytroherez.savingstrack.server.data.tables.GoalsTable.createdAt
import com.dmytroherez.savingstrack.server.data.tables.GoalsTable.currency
import com.dmytroherez.savingstrack.server.data.tables.GoalsTable.deadline
import com.dmytroherez.savingstrack.server.data.tables.GoalsTable.firebaseUid
import com.dmytroherez.savingstrack.server.data.tables.GoalsTable.id
import com.dmytroherez.savingstrack.server.data.tables.GoalsTable.targetAmount
import com.dmytroherez.savingstrack.server.data.tables.GoalsTable.title
import com.dmytroherez.savingstrack.server.data.tables.TransactionsTable
import com.dmytroherez.savingstrack.server.dbQuery
import com.dmytroherez.savingstrack.server.domain.repo.GoalsRepo
import com.dmytroherez.savingstrack.shared.dto.goals.CreateGoalRequest
import com.dmytroherez.savingstrack.shared.dto.goals.GoalForTransactionItem
import com.dmytroherez.savingstrack.shared.dto.goals.GoalItem
import com.dmytroherez.savingstrack.shared.dto.transactions.TransactionItem
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.leftJoin
import org.jetbrains.exposed.v1.core.max
import org.jetbrains.exposed.v1.core.sum
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.update
import kotlin.time.Clock

class GoalsRepoImpl : GoalsRepo {
    override suspend fun addGoal(userId: String, request: CreateGoalRequest) {
        dbQuery {
            GoalsTable
                .insert {
                    it[firebaseUid] = userId
                    it[title] = request.title
                    it[targetAmount] = request.targetAmountInMinorUnits
                    it[currency] = request.currency
                    it[deadline] = request.deadline
                }[id]
        }
    }

    override suspend fun getGoals(userId: String): List<GoalItem> {
        return dbQuery {
            val currentAmountSum = TransactionsTable.amount.sum()

            val goals = GoalsTable.leftJoin(TransactionsTable, { id }, { TransactionsTable.goalId })
                .select(GoalsTable.columns + currentAmountSum)
                .where { firebaseUid eq userId }
                .groupBy(*GoalsTable.columns.toTypedArray())
                .map { row ->
                    val targetAmount = row[targetAmount]
                    val currentAmount = row[currentAmountSum] ?: 0L

                    GoalItem(
                        id = row[id],
                        title = row[title],
                        targetAmount = targetAmount,
                        currentAmount = currentAmount,
                        currency = row[currency],
                        progress = calculateProgress(targetAmount, currentAmount),
                        deadline = row[deadline],
                        createdAt = row[createdAt],
                        completedAt = row[completedAt],
                        recentTransactions = emptyList(), // init value, calculated by the code below
                        hasMoreTransactions = false  // init value, calculated by the code below
                    )
                }

            goals.map { goal ->
                val latestTransactionsQuery = TransactionsTable
                    .select(TransactionsTable.columns)
                    .where { TransactionsTable.goalId eq goal.id }
                    .orderBy(TransactionsTable.createdAt to SortOrder.DESC)
                    .limit(4)
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

                val hasMore = latestTransactionsQuery.size > 3
                val displayTransactions = latestTransactionsQuery.take(3)

                goal.copy(
                    recentTransactions = displayTransactions,
                    hasMoreTransactions = hasMore
                )
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

    override suspend fun getGoalsForTransaction(userId: String): List<GoalForTransactionItem> {
        return dbQuery {
            val currentAmountSum = TransactionsTable.amount.sum()
            val lastTransactionTime = TransactionsTable.createdAt.max()

            val goalsTableColumns = listOf(id, title, targetAmount)
            val aggregatedColumns = listOf(currentAmountSum, lastTransactionTime)

            GoalsTable.leftJoin(TransactionsTable, { id }, { TransactionsTable.goalId })
                .select(goalsTableColumns + aggregatedColumns)
                .where { (firebaseUid eq userId) and (completedAt eq null) }
                .groupBy(*goalsTableColumns.toTypedArray())
                .map { row ->
                    val targetAmount = row[targetAmount]
                    val currentAmount = row[currentAmountSum] ?: 0L

                    GoalForTransactionItem(
                        id = row[id],
                        title = row[title],
                        lastTransaction = row[lastTransactionTime],
                        progress = calculateProgress(targetAmount, currentAmount),
                        targetAmount = targetAmount,
                        currentAmount = currentAmount
                    )
                }
        }
    }
    private fun calculateProgress(targetAmount: Long, currentAmount: Long): Float {
        return if (targetAmount > 0L) {
            currentAmount.toFloat() / targetAmount.toFloat()
        } else {
            0f
        }
    }
}
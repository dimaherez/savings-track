package com.dmytroherez.savingstrack.data.repo

import com.dmytroherez.savingstrack.data.tables.GoalsTable
import com.dmytroherez.savingstrack.data.tables.TransactionsTable
import com.dmytroherez.savingstrack.data.tables.TransactionsTable.amount
import com.dmytroherez.savingstrack.data.tables.TransactionsTable.category
import com.dmytroherez.savingstrack.data.tables.TransactionsTable.createdAt
import com.dmytroherez.savingstrack.data.tables.TransactionsTable.currency
import com.dmytroherez.savingstrack.data.tables.TransactionsTable.description
import com.dmytroherez.savingstrack.dbQuery
import com.dmytroherez.savingstrack.domain.model.TransactionsAggregatedTotal
import com.dmytroherez.savingstrack.domain.repo.TransactionsRepo
import com.dmytroherez.savingstrack.dto.goals.GoalForTransactionItem
import com.dmytroherez.savingstrack.dto.transactions.CurrencyTotal
import com.dmytroherez.savingstrack.dto.transactions.DashboardResponse
import com.dmytroherez.savingstrack.dto.transactions.PostTransactionRequest
import com.dmytroherez.savingstrack.dto.transactions.TransactionItem
import com.dmytroherez.savingstrack.dto.transactions.TransactionsByCurrencyResponse
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.sum
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll

class TransactionsRepoImpl : TransactionsRepo {
    override suspend fun addTransaction(uid: String, transaction: PostTransactionRequest) {
        dbQuery {
            TransactionsTable.insert {
                it[currency] = transaction.currency
                it[amount] = transaction.amount
                it[description] = transaction.description
                it[firebaseUid] = uid
                it[category] = transaction.category
                it[goalId] = transaction.goalId
            }[TransactionsTable.id]
        }
    }

    override suspend fun getAllTransactions(
        userId: String,
        currency: String
    ): TransactionsByCurrencyResponse {
        return dbQuery {
            val transactionsRaw = TransactionsTable
                .selectAll()
                .where { (TransactionsTable.firebaseUid eq userId) and (TransactionsTable.currency eq currency)}
                .orderBy(createdAt, SortOrder.DESC)

            TransactionsByCurrencyResponse (
                totalAmount = transactionsRaw.map { it[amount] }.reduce { amount1, amount2 -> amount1 + amount2},
                transactions = transactionsRaw.map {
                    TransactionItem(
                        id = it[TransactionsTable.id],
                        currency = it[TransactionsTable.currency],
                        amount = it[amount],
                        userId = it[TransactionsTable.firebaseUid],
                        description = it[description],
                        createdAt = it[createdAt],
                        category = it[category]
                    )
                }
            )
        }
    }

    override suspend fun getTransactionsDashboard(userId: String): DashboardResponse {
        return dbQuery {
            val totalAmount = amount.sum()

            val aggregatedTotals = TransactionsTable
                .select(category, currency, totalAmount)
                .where { TransactionsTable.firebaseUid eq userId }
                .groupBy(category, currency)
                .map { row ->
                    val category = row[category]
                    val currency = row[currency]
                    val sum = row[totalAmount] ?: 0.0
                    TransactionsAggregatedTotal(category, currency, sum)
                }

            val transactions = TransactionsTable
                .selectAll()
                .where {TransactionsTable.firebaseUid eq userId}
                .orderBy(createdAt to SortOrder.DESC)
                .map { row ->
                    TransactionItem(
                        id = row[TransactionsTable.id],
                        userId = row[TransactionsTable.firebaseUid],
                        category = row[category],
                        currency = row[currency],
                        amount = row[amount],
                        description = row[description],
                        createdAt = row[createdAt]
                    )
                }

            val goals = GoalsTable
                .select(
                    GoalsTable.id,
                    GoalsTable.title
                )
                .where { GoalsTable.firebaseUid eq userId }
                .orderBy(GoalsTable.createdAt to SortOrder.DESC)
                .map {
                    GoalForTransactionItem(
                        id = it[GoalsTable.id],
                        title = it[GoalsTable.title]
                    )
                }

            val categoriesMap =
                aggregatedTotals.map { it.category }.associateWith { category ->
                    aggregatedTotals
                        .filter { it.category == category }
                        .map {
                            val recentTransactions =
                                transactions.filter { item -> item.currency == it.currency }
                            CurrencyTotal(
                                currency = it.currency,
                                totalAmount = it.sum,
                                recentTransactions = recentTransactions.take(3),
                                hasMoreTransactions = recentTransactions.size > 3
                            )
                        }
                }

            DashboardResponse(
                categories = categoriesMap,
                availableGoals = goals
            )
        }
    }
}

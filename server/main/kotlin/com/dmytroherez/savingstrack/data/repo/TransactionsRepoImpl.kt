package com.dmytroherez.savingstrack.data.repo

import com.dmytroherez.savingstrack.data.tables.TransactionsTable
import com.dmytroherez.savingstrack.data.tables.TransactionsTable.amount
import com.dmytroherez.savingstrack.data.tables.TransactionsTable.category
import com.dmytroherez.savingstrack.data.tables.TransactionsTable.createdAt
import com.dmytroherez.savingstrack.data.tables.TransactionsTable.currency
import com.dmytroherez.savingstrack.data.tables.TransactionsTable.description
import com.dmytroherez.savingstrack.domain.repo.TransactionsRepo
import com.dmytroherez.savingstrack.dto.savings.TransactionItem
import com.dmytroherez.savingstrack.dto.savings.PostTransactionRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import kotlin.time.ExperimentalTime

class TransactionsRepoImpl : TransactionsRepo {
    private suspend fun <T> dbQuery(block: suspend () -> T): T = withContext(Dispatchers.IO) {
        suspendTransaction { block() }
    }

    override suspend fun addTransaction(uid: String, transaction: PostTransactionRequest) {
        dbQuery {
            TransactionsTable.insert {
                it[currency] = transaction.currency
                it[amount] = transaction.amount
                it[description] = transaction.description
                it[userId] = uid
                it[category] = transaction.category
            }[TransactionsTable.id]
        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun getAllTransactions(userId: String): List<TransactionItem> {
        return dbQuery {
            TransactionsTable
                .selectAll()
                .where { TransactionsTable.userId eq userId }
                .map {
                    TransactionItem(
                        id = it[TransactionsTable.id],
                        currency = it[currency],
                        amount = it[amount],
                        userId = it[TransactionsTable.userId],
                        description = it[description],
                        createdAt = it[createdAt],
                        category = it[category]
                    )
                }
        }
    }
}

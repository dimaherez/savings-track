package com.dmytroherez.savingstrack.data.repo

import com.dmytroherez.savingstrack.data.tables.SavingsTable
import com.dmytroherez.savingstrack.data.tables.SavingsTable.amount
import com.dmytroherez.savingstrack.data.tables.SavingsTable.category
import com.dmytroherez.savingstrack.data.tables.SavingsTable.createdAt
import com.dmytroherez.savingstrack.data.tables.SavingsTable.currency
import com.dmytroherez.savingstrack.data.tables.SavingsTable.description
import com.dmytroherez.savingstrack.dto.savings.SavingItem
import com.dmytroherez.savingstrack.dto.savings.PostSavingRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import kotlin.time.ExperimentalTime

class SavingsRepository {
    private suspend fun <T> dbQuery(block: suspend () -> T): T = withContext(Dispatchers.IO) {
        suspendTransaction { block() }
    }

    suspend fun addSaving(uid: String, saving: PostSavingRequest) {
        dbQuery {
            SavingsTable.insert {
                it[currency] = saving.currency
                it[amount] = saving.amount
                it[description] = saving.description
                it[userId] = uid
                it[category] = saving.category
            }[SavingsTable.id]
        }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun getAllSavings(userId: String): List<SavingItem> {
        return dbQuery {
            SavingsTable
                .selectAll()
                .where { SavingsTable.userId eq userId }
                .map {
                    SavingItem(
                        id = it[SavingsTable.id],
                        currency = it[currency],
                        amount = it[amount],
                        userId = it[SavingsTable.userId],
                        description = it[description],
                        createdAt = it[createdAt],
                        category = it[category]
                    )
                }
        }
    }
}

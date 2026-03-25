package com.savingstrack.repo

import com.savingstrack.tables.SavingsTable
import com.savingstrack.tables.SavingsTable.amount
import com.savingstrack.tables.SavingsTable.createdAt
import com.savingstrack.tables.SavingsTable.currency
import com.savingstrack.tables.SavingsTable.description
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.dto.savings.GetSavingsResponseItem
import org.example.dto.savings.PostSavingRequest
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import kotlin.time.ExperimentalTime

class SavingsRepository {
    private suspend fun <T> dbQuery(block: suspend () -> T): T = withContext(Dispatchers.IO) {
        suspendTransaction { block() }
    }


    suspend fun addSaving(saving: PostSavingRequest): Int {
        return dbQuery {
            SavingsTable.insert {
                it[currency] = saving.currency
                it[amount] = saving.amount
                it[description] = saving.description
                it[userId] = saving.userId
            }[SavingsTable.id]
        }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun getAllSavings(userId: String): List<GetSavingsResponseItem> {
        return dbQuery {
            SavingsTable
                .selectAll()
                .where { SavingsTable.userId eq userId }
                .map {
                    GetSavingsResponseItem(
                        id = it[SavingsTable.id],
                        currency = it[currency],
                        amount = it[amount],
                        userId = it[SavingsTable.userId],
                        description = it[description],
                        createdAt = it[createdAt]
                    )
                }
        }
    }
}
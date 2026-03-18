package com.savingstrack.repo

import com.savingstrack.tables.SavingsTable
import com.savingstrack.tables.SavingsTable.amount
import com.savingstrack.tables.SavingsTable.currency
import kotlinx.coroutines.Dispatchers
import org.example.dto.SavingsDto
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class SavingsRepository {
    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun addSaving(saving: SavingsDto): Int {
        return dbQuery {
            SavingsTable.insert {
                it[currency] = saving.currency
                it[amount] = saving.amount
                it[description] = saving.description
            }[SavingsTable.id]
        }
    }

    suspend fun getAllSavings(): List<SavingsDto> {
        return dbQuery {
            SavingsTable.selectAll().map {
                SavingsDto(
                    id = it[SavingsTable.id],
                    currency = it[currency],
                    amount = it[amount],
                    description = it[SavingsTable.description]
                )
            }
        }
    }
}
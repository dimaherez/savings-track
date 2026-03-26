package com.dmytroherez.savingstrack.data.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp
import kotlin.time.ExperimentalTime

object SavingsTable : Table("savings") {
    val id = integer("id").autoIncrement()
    val userId = varchar("userId", 128)
    val currency = varchar("currency", 10)
    val amount = double("amount")
    val description = varchar("description", 255).nullable()
    @OptIn(ExperimentalTime::class)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)

    override val primaryKey = PrimaryKey(id)
}

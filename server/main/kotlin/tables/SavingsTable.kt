package com.savingstrack.tables

import org.jetbrains.exposed.sql.Table

object SavingsTable : Table("savings") {
    val id = integer("id").autoIncrement()
    val currency = varchar("currency", 10)
    val amount = double("amount")
    val description = varchar("description", 255).nullable()

    override val primaryKey = PrimaryKey(id)
}
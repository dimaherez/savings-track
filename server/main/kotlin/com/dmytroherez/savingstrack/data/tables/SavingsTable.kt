package com.dmytroherez.savingstrack.data.tables

import com.dmytroherez.savingstrack.Constants.FIELD_ID
import com.dmytroherez.savingstrack.Constants.FIELD_USER_ID
import com.dmytroherez.savingstrack.Constants.FIELD_AMOUNT
import com.dmytroherez.savingstrack.Constants.FIELD_CURRENCY
import com.dmytroherez.savingstrack.Constants.FIELD_DESCRIPTION
import com.dmytroherez.savingstrack.Constants.FIELD_CREATED_AT
import com.dmytroherez.savingstrack.Constants.TABLE_SAVINGS
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp
import kotlin.time.ExperimentalTime

object SavingsTable : Table(TABLE_SAVINGS) {
    val id = integer(FIELD_ID).autoIncrement()
    val userId = varchar(FIELD_USER_ID, 128)
    val currency = varchar(FIELD_CURRENCY, 10)
    val amount = double(FIELD_AMOUNT)
    val description = varchar(FIELD_DESCRIPTION, 255).nullable()
    @OptIn(ExperimentalTime::class)
    val createdAt = timestamp(FIELD_CREATED_AT).defaultExpression(CurrentTimestamp)

    override val primaryKey = PrimaryKey(id)
}

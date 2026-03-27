package com.dmytroherez.savingstrack.data.tables

import com.dmytroherez.savingstrack.Constants.FIELD_ID
import com.dmytroherez.savingstrack.Constants.FIELD_USER_ID
import com.dmytroherez.savingstrack.Constants.FIELD_AMOUNT
import com.dmytroherez.savingstrack.Constants.FIELD_CATEGORY
import com.dmytroherez.savingstrack.Constants.FIELD_CURRENCY
import com.dmytroherez.savingstrack.Constants.FIELD_DESCRIPTION
import com.dmytroherez.savingstrack.Constants.FIELD_CREATED_AT
import com.dmytroherez.savingstrack.Constants.TABLE_TRANSACTIONS
import com.dmytroherez.savingstrack.dto.savings.SavingCategory
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp
import kotlin.time.ExperimentalTime

object TransactionsTable : Table(TABLE_TRANSACTIONS) {
    val id = integer(FIELD_ID).autoIncrement()
    val userId = varchar(FIELD_USER_ID, 128)
    val currency = varchar(FIELD_CURRENCY, 10)
    val amount = double(FIELD_AMOUNT)
    val description = varchar(FIELD_DESCRIPTION, 255).nullable()
    val category = enumerationByName(FIELD_CATEGORY, 50, SavingCategory::class)

    @OptIn(ExperimentalTime::class)
    val createdAt = timestamp(FIELD_CREATED_AT).defaultExpression(CurrentTimestamp)

    override val primaryKey = PrimaryKey(id)
}

package com.dmytroherez.savingstrack.server.data.tables

import com.dmytroherez.savingstrack.server.Constants.FIELD_AMOUNT
import com.dmytroherez.savingstrack.server.Constants.FIELD_CATEGORY
import com.dmytroherez.savingstrack.server.Constants.FIELD_CREATED_AT
import com.dmytroherez.savingstrack.server.Constants.FIELD_CURRENCY
import com.dmytroherez.savingstrack.server.Constants.FIELD_DESCRIPTION
import com.dmytroherez.savingstrack.server.Constants.FIELD_GOAL_ID
import com.dmytroherez.savingstrack.server.Constants.FIELD_ID
import com.dmytroherez.savingstrack.server.Constants.FIELD_FIREBASE_UID
import com.dmytroherez.savingstrack.server.Constants.TABLE_TRANSACTIONS
import com.dmytroherez.savingstrack.shared.dto.transactions.SavingCategory
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

object TransactionsTable : Table(TABLE_TRANSACTIONS) {
    val id = integer(FIELD_ID).autoIncrement()
    val firebaseUid = varchar(FIELD_FIREBASE_UID, 128)
    val currency = varchar(FIELD_CURRENCY, 10)
    val amount = long(FIELD_AMOUNT)
    val description = varchar(FIELD_DESCRIPTION, 255).nullable()
    val category = enumerationByName(FIELD_CATEGORY, 50, SavingCategory::class)
    val createdAt = timestamp(FIELD_CREATED_AT).defaultExpression(CurrentTimestamp)
    val goalId = integer(FIELD_GOAL_ID).references(GoalsTable.id).nullable()

    override val primaryKey = PrimaryKey(id)
}

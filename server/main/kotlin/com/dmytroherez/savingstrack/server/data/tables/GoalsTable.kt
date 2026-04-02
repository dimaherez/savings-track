package com.dmytroherez.savingstrack.server.data.tables

import com.dmytroherez.savingstrack.server.Constants.FIELD_COMPLETED_AT
import com.dmytroherez.savingstrack.server.Constants.FIELD_CREATED_AT
import com.dmytroherez.savingstrack.server.Constants.FIELD_CURRENCY
import com.dmytroherez.savingstrack.server.Constants.FIELD_DEADLINE
import com.dmytroherez.savingstrack.server.Constants.FIELD_ID
import com.dmytroherez.savingstrack.server.Constants.FIELD_TARGET_AMOUNT
import com.dmytroherez.savingstrack.server.Constants.FIELD_TITLE
import com.dmytroherez.savingstrack.server.Constants.FIELD_FIREBASE_UID
import com.dmytroherez.savingstrack.server.Constants.TABLE_GOALS
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.timestamp

object GoalsTable : Table(TABLE_GOALS) {
    val id = integer(FIELD_ID).autoIncrement()
    val firebaseUid = varchar(FIELD_FIREBASE_UID, 128)
    val title = varchar(FIELD_TITLE, 255)
    val targetAmount = long(FIELD_TARGET_AMOUNT)
    val currency = varchar(FIELD_CURRENCY, 3)
    val deadline = date(FIELD_DEADLINE).nullable()
    val createdAt = timestamp(FIELD_CREATED_AT).defaultExpression(CurrentTimestamp)
    val completedAt = timestamp(FIELD_COMPLETED_AT).nullable()

    override val primaryKey = PrimaryKey(id)
}
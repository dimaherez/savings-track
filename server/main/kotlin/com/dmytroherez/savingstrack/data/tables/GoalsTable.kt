package com.dmytroherez.savingstrack.data.tables

import com.dmytroherez.savingstrack.Constants.FIELD_CREATED_AT
import com.dmytroherez.savingstrack.Constants.FIELD_CURRENCY
import com.dmytroherez.savingstrack.Constants.FIELD_DEADLINE
import com.dmytroherez.savingstrack.Constants.FIELD_ID
import com.dmytroherez.savingstrack.Constants.FIELD_TARGET_AMOUNT
import com.dmytroherez.savingstrack.Constants.FIELD_TITLE
import com.dmytroherez.savingstrack.Constants.FIELD_FIREBASE_UID
import com.dmytroherez.savingstrack.Constants.TABLE_GOALS
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.timestamp

object GoalsTable : Table(TABLE_GOALS) {
    val id = integer(FIELD_ID).autoIncrement()
    val firebaseUid = varchar(FIELD_FIREBASE_UID, 128)
    val title = varchar(FIELD_TITLE, 255)
    val targetAmount = double(FIELD_TARGET_AMOUNT)
    val currency = varchar(FIELD_CURRENCY, 3)
    val deadline = date(FIELD_DEADLINE).nullable()
    val createdAt = timestamp(FIELD_CREATED_AT).defaultExpression(CurrentTimestamp)

    override val primaryKey = PrimaryKey(id)
}
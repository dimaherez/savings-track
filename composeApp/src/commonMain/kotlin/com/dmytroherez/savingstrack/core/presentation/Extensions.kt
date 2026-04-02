package com.dmytroherez.savingstrack.core.presentation

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

object Extensions {
    fun Long.toDisplayAmount() = this / 100.0
    fun String.toAmountMinorUnits() = this.toDoubleOrNull()?.let { (it * 100).toLong() } ?: 0L

    fun Instant?.toDisplayDateTimeString(): String {
        this ?: return "null"
        return toLocalDateTime(TimeZone.currentSystemDefault()).format(
            LocalDateTime.Format {
                day(); char('.'); monthNumber(); char('.'); year()
                char(' '); hour(); char(':'); minute()
            }
        )
    }
    fun Instant?.toDisplayDateString(): String {
        this ?: return "null"
        return toLocalDateTime(TimeZone.currentSystemDefault()).format(
            LocalDateTime.Format {
                day(); char('.'); monthNumber(); char('.'); year()
            }
        )
    }

    fun LocalDate?.toDisplayDateString(): String {
        this ?: return "null"
        return format(
            LocalDate.Format {
                day(); char('.'); monthNumber(); char('.'); year()
            }
        )
    }
}
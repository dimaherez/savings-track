package com.dmytroherez.savingstrack.core.presentation

object Extensions {
    fun Long.toDisplayAmount() = this / 100.0
    fun String.toAmountMinorUnits() = this.toDoubleOrNull()?.let { (it * 100).toLong() } ?: 0L
}
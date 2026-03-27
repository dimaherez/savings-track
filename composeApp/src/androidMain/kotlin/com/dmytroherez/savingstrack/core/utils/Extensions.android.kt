package com.dmytroherez.savingstrack.core.utils

import java.text.NumberFormat
import java.util.Currency

actual fun formatAsFiat(amount: Double, currencyCode: String, showSign: Boolean): String {
    val formattedAmount = NumberFormat.getCurrencyInstance().apply {
        maximumFractionDigits = 2
        currency = Currency.getInstance(currencyCode)
    }.format(amount)

    return if (showSign && amount > 0) {
        "+$formattedAmount"
    } else {
        formattedAmount
    }
}

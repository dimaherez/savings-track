package com.dmytroherez.savingstrack.core.utils

import co.touchlab.kermit.Logger
import java.text.NumberFormat
import java.util.Currency

actual fun formatAsFiat(amount: Long, currencyCode: String, showSign: Boolean): String {
    return try {
        val formattedAmount = NumberFormat.getCurrencyInstance().apply {
            maximumFractionDigits = 2
            currency = Currency.getInstance(currencyCode)
        }.format(amount / 100.0)

        if (showSign && amount > 0) {
            "+$formattedAmount"
        } else {
            formattedAmount
        }
    } catch (e: Exception) {
        Logger.e(e) { "Failed to format amount as fiat string amount=$amount; currencyCode=$currencyCode" }
        ""
    }
}

package com.dmytroherez.savingstrack.core.utils

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle

actual fun formatAsFiat(amount: Double, currencyCode: String, showSign: Boolean): String {
    val formattedAmount = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterCurrencyStyle
        this.currencyCode = currencyCode
    }.stringFromNumber(NSNumber(amount)) ?: "$amount $currencyCode"

    return if (showSign && amount > 0) {
        "+$formattedAmount"
    } else {
        formattedAmount
    }
}
package com.dmytroherez.savingstrack.dto.savings

import kotlinx.serialization.Serializable

@Serializable
enum class SavingCategory {
    FIAT,
    CRYPTO,
    STOCK,
    REAL_ESTATE
}
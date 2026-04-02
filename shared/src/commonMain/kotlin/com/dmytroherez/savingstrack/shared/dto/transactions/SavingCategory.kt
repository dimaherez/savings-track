package com.dmytroherez.savingstrack.shared.dto.transactions

import kotlinx.serialization.Serializable

@Serializable
enum class SavingCategory {
    FIAT,
    CRYPTO,
    STOCK,
    REAL_ESTATE
}
package com.dmytroherez.savingstrack.domain.repo

import com.dmytroherez.savingstrack.dto.savings.SavingItem

interface SavingsRepo {
    suspend fun postSaving(
        currency: String,
        amount: Double,
        description: String? = null
    ): Result<Unit>
    suspend fun getSavings(): Result<List<SavingItem>>
}
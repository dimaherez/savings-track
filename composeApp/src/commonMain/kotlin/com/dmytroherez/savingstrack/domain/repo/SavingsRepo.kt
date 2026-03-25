package com.dmytroherez.savingstrack.domain.repo

import com.dmytroherez.savingstrack.dto.savings.GetSavingsResponseItem

interface SavingsRepo {
    suspend fun postSaving(
        userId: String,
        currency: String,
        amount: Double,
        description: String? = null
    ): Result<Unit>
    suspend fun getSavings(): Result<List<GetSavingsResponseItem>>
}
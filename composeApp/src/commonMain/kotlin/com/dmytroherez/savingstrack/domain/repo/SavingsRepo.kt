package com.dmytroherez.savingstrack.domain.repo

import com.dmytroherez.savingstrack.dto.savings.PostSavingRequest
import com.dmytroherez.savingstrack.dto.savings.SavingItem

interface SavingsRepo {
    suspend fun postSaving(
        request: PostSavingRequest
    ): Result<Unit>
    suspend fun getSavings(): Result<List<SavingItem>>
}
package com.dmytroherez.savingstrack.domain.repo

import com.dmytroherez.savingstrack.dto.transactions.DashboardResponse
import com.dmytroherez.savingstrack.dto.transactions.PostTransactionRequest
import com.dmytroherez.savingstrack.dto.transactions.TransactionItem

interface SavingsRepo {
    suspend fun postSaving(
        request: PostTransactionRequest
    ): Result<Unit>
    suspend fun getSavings(currency: String): Result<List<TransactionItem>>
    suspend fun getSavingsDashboard() : Result<DashboardResponse>
}
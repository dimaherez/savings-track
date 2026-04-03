package com.dmytroherez.savingstrack.domain.repo

import com.dmytroherez.savingstrack.shared.dto.transactions.DashboardResponse
import com.dmytroherez.savingstrack.shared.dto.transactions.PostTransactionRequest
import com.dmytroherez.savingstrack.shared.dto.transactions.TransactionsByCurrencyResponse
import kotlinx.coroutines.flow.SharedFlow

interface SavingsRepo {
    suspend fun postSaving(
        request: PostTransactionRequest
    ): Result<Unit>
    suspend fun getTransactionsByCurrency(currency: String): Result<TransactionsByCurrencyResponse>
    suspend fun getSavingsDashboard() : Result<DashboardResponse>

    val refreshTrigger: SharedFlow<Unit>
}
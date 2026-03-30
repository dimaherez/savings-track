package com.dmytroherez.savingstrack.domain.repo

import com.dmytroherez.savingstrack.dto.transactions.DashboardResponse
import com.dmytroherez.savingstrack.dto.transactions.PostTransactionRequest
import com.dmytroherez.savingstrack.dto.transactions.TransactionItem
import com.dmytroherez.savingstrack.dto.transactions.TransactionsByCurrencyResponse

interface SavingsRepo {
    suspend fun postSaving(
        request: PostTransactionRequest
    ): Result<Unit>
    suspend fun getTransactionsByCurrency(currency: String): Result<TransactionsByCurrencyResponse>
    suspend fun getSavingsDashboard() : Result<DashboardResponse>
}
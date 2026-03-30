package com.dmytroherez.savingstrack.domain.repo

import com.dmytroherez.savingstrack.dto.transactions.DashboardResponse
import com.dmytroherez.savingstrack.dto.transactions.PostTransactionRequest
import com.dmytroherez.savingstrack.dto.transactions.TransactionsByCurrencyResponse

interface TransactionsRepo {
    suspend fun addTransaction(uid: String, transaction: PostTransactionRequest)
    suspend fun getAllTransactions(userId: String, currency: String): TransactionsByCurrencyResponse
    suspend fun getTransactionsDashboard(userId: String): DashboardResponse
}
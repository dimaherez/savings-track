package com.dmytroherez.savingstrack.domain.repo

import com.dmytroherez.savingstrack.dto.transactions.DashboardResponse
import com.dmytroherez.savingstrack.dto.transactions.PostTransactionRequest
import com.dmytroherez.savingstrack.dto.transactions.TransactionItem

interface TransactionsRepo {
    suspend fun addTransaction(uid: String, transaction: PostTransactionRequest)
    suspend fun getAllTransactions(userId: String): List<TransactionItem>
    suspend fun getTransactionsDashboard(userId: String): DashboardResponse
}
package com.dmytroherez.savingstrack.domain.repo

import com.dmytroherez.savingstrack.dto.savings.PostTransactionRequest
import com.dmytroherez.savingstrack.dto.savings.TransactionItem

interface TransactionsRepo {
    suspend fun addTransaction(uid: String, transaction: PostTransactionRequest)
    suspend fun getAllTransactions(userId: String): List<TransactionItem>

}
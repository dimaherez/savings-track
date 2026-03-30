package com.dmytroherez.savingstrack.presentation.transactions

import com.dmytroherez.savingstrack.core.presentation.BaseUiState
import com.dmytroherez.savingstrack.dto.transactions.TransactionItem

data class TransactionsState(
    val transactions: List<TransactionItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val isLoading: Boolean = false,
    val currency: String = ""
) : BaseUiState

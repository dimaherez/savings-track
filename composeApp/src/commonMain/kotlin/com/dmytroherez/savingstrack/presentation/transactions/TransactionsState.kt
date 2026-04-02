package com.dmytroherez.savingstrack.presentation.transactions

import com.dmytroherez.savingstrack.core.presentation.BaseUiState
import com.dmytroherez.savingstrack.shared.dto.transactions.TransactionItem

data class TransactionsState(
    val transactions: List<TransactionItem> = emptyList(),
    val totalAmount: Long = 0L,
    val isLoading: Boolean = false,
    val currency: String = ""
) : BaseUiState

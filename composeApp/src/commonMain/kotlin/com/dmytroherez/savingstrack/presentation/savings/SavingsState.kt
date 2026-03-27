package com.dmytroherez.savingstrack.presentation.savings

import com.dmytroherez.savingstrack.core.presentation.BaseUiState
import com.dmytroherez.savingstrack.dto.savings.TransactionItem

data class SavingsState(
    val savings: List<TransactionItem> = emptyList(),
    val showAddSavingDialog: Boolean = false,
    val isAddSavingLoading: Boolean = false,
    val isSavingsListLoading: Boolean = false
) : BaseUiState

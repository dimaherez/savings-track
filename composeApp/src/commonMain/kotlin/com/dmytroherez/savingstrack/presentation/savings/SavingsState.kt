package com.dmytroherez.savingstrack.presentation.savings

import com.dmytroherez.savingstrack.core.presentation.BaseUiState
import com.dmytroherez.savingstrack.dto.transactions.DashboardResponse
import com.dmytroherez.savingstrack.dto.transactions.TransactionItem

data class SavingsState(
    val savings: DashboardResponse? = null,
    val showAddSavingDialog: Boolean = false,
    val isAddSavingLoading: Boolean = false,
    val isSavingsListLoading: Boolean = false
) : BaseUiState

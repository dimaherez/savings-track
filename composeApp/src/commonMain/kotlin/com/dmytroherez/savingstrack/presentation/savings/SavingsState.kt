package com.dmytroherez.savingstrack.presentation.savings

import com.dmytroherez.savingstrack.core.presentation.BaseUiState
import com.dmytroherez.savingstrack.dto.transactions.DashboardResponse
import com.dmytroherez.savingstrack.dto.transactions.SavingCategory
import com.dmytroherez.savingstrack.dto.transactions.TransactionItem

data class SavingsState(
    val savings: DashboardResponse? = null,
    val showAddSavingDialog: Boolean = false,
    val isAddSavingLoading: Boolean = false,
    val isSavingsListLoading: Boolean = false
) : BaseUiState

data class AddTransactionState(
    val currency: String = "USD",
    val amount: String = "",
    val category: SavingCategory = SavingCategory.FIAT,
    val description: String = "",
    val goalId: Int? = null
)

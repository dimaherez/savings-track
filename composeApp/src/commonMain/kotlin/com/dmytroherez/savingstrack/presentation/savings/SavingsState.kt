package com.dmytroherez.savingstrack.presentation.savings

import com.dmytroherez.savingstrack.core.presentation.BaseUiState
import com.dmytroherez.savingstrack.dto.savings.PostSavingRequest
import com.dmytroherez.savingstrack.dto.savings.SavingItem

data class SavingsState(
    val savings: List<SavingItem> = emptyList(),
    val showAddSavingDialog: Boolean = false,
    val isAddSavingLoading: Boolean = false,
    val isSavingsListLoading: Boolean = false
) : BaseUiState

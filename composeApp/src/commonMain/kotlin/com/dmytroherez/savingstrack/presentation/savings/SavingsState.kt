package com.dmytroherez.savingstrack.presentation.savings

import com.dmytroherez.savingstrack.core.presentation.BaseUiState
import com.dmytroherez.savingstrack.dto.savings.SavingItem

data class FiatState(
    val something: String = "",
    val cryptoSavings: CryptoSavings = CryptoSavings,
    val fiatSavings: FiatSavings = FiatSavings,
    val savings: List<SavingItem> = emptyList()
) : BaseUiState

data object CryptoSavings
data object FiatSavings

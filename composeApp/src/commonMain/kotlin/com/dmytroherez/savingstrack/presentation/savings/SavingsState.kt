package com.dmytroherez.savingstrack.presentation.savings

import com.dmytroherez.savingstrack.core.presentation.BaseUiState

data class FiatState(
    val something: String = "",
    val cryptoSavings: CryptoSavings = CryptoSavings,
    val fiatSavings: FiatSavings = FiatSavings
) : BaseUiState

data object CryptoSavings
data object FiatSavings

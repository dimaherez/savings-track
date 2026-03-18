package com.dmytroherez.savingstrack.presentation.fiat

import com.dmytroherez.savingstrack.core.presentation.BaseUiState
import com.dmytroherez.savingstrack.domain.enums.SavingsPagerTab

data class FiatState(
    val something: String = "",
    val cryptoSavings: CryptoSavings = CryptoSavings,
    val fiatSavings: FiatSavings = FiatSavings
) : BaseUiState

data object CryptoSavings
data object FiatSavings

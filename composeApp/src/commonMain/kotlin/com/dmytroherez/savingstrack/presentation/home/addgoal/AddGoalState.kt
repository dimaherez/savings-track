package com.dmytroherez.savingstrack.presentation.home.addgoal

import com.dmytroherez.savingstrack.core.presentation.BaseUiState
import kotlinx.datetime.LocalDate

data class AddGoalState (
    val availableCurrencies: List<String> = listOf("USD", "EUR", "UAH"),
    val title: String = "",
    val amount: String = "",
    val currency: String = "USD",
    val selectedDate: LocalDate? = null,
    val showDatePicker: Boolean = false,
    val isLoading: Boolean = false
) : BaseUiState
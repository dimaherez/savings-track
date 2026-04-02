package com.dmytroherez.savingstrack.presentation.income

import com.dmytroherez.savingstrack.core.presentation.BaseAction

sealed interface IncomeAction : BaseAction {
    data object SomeAction : IncomeAction
}

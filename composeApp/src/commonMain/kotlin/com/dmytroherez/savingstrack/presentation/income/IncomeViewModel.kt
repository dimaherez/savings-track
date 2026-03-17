package com.dmytroherez.savingstrack.presentation.income

import com.dmytroherez.savingstrack.core.presentation.BaseViewModel

class IncomeViewModel : BaseViewModel<IncomeState, IncomeEvent>(
    IncomeState()
) {

    fun onAction(action: IncomeAction) {
        when (action) {
            IncomeAction.SomeAction -> {

            }
        }
    }
}

package com.dmytroherez.savingstrack.presentation.income

import com.dmytroherez.savingstrack.core.presentation.BaseViewModel

class IncomeViewModel : BaseViewModel<IncomeState, IncomeEvent, IncomeAction>(
    IncomeState()
) {

    override fun onAction(action: IncomeAction) {
        when (action) {
            IncomeAction.SomeAction -> {

            }
        }
    }
}

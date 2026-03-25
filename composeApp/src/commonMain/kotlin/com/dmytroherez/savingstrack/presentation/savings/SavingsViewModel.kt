package com.dmytroherez.savingstrack.presentation.savings

import com.dmytroherez.savingstrack.core.presentation.BaseViewModel

class SavingsViewModel : BaseViewModel<FiatState, SavingsEvent>(
    FiatState()
) {

    fun onAction(action: SavingsAction) {
        when (action) {
            SavingsAction.SomeAction -> {

            }
        }
    }
}

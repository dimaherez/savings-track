package com.dmytroherez.savingstrack.presentation.fiat

import com.dmytroherez.savingstrack.core.presentation.BaseViewModel

class FiatViewModel : BaseViewModel<FiatState, FiatEvent>(
    FiatState()
) {

    fun onAction(action: FiatAction) {
        when (action) {
            FiatAction.SomeAction -> {

            }
        }
    }
}

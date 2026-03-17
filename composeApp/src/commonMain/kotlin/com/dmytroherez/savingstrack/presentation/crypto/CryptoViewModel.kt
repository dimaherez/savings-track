package com.dmytroherez.savingstrack.presentation.crypto

import com.dmytroherez.savingstrack.core.presentation.BaseViewModel

class CryptoViewModel : BaseViewModel<CryptoState, CryptoEvent>(
    CryptoState()
) {

    fun onAction(action: CryptoAction) {
        when (action) {
            CryptoAction.SomeAction -> {

            }
        }
    }
}

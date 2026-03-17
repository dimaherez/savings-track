package com.dmytroherez.savingstrack.presentation.crypto

sealed interface CryptoAction {
    data object SomeAction : CryptoAction
}

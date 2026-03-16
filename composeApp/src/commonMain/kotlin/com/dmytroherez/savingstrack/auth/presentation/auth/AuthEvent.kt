package com.dmytroherez.savingstrack.auth.presentation.auth

import com.dmytroherez.savingstrack.core.presentation.BaseUiEvent

sealed interface AuthEvent : BaseUiEvent {
    data object LoginSuccess : AuthEvent
}
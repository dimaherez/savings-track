package com.dmytroherez.savingstrack.presentation.auth

import com.dmytroherez.savingstrack.core.presentation.BaseUiEvent
import com.dmytroherez.savingstrack.core.presentation.UiText

sealed interface AuthEvent : BaseUiEvent {
    data object LoginSuccess : AuthEvent
    data object RegisterSuccess : AuthEvent
    data class ShowErrorToast(val message: UiText) : AuthEvent
}
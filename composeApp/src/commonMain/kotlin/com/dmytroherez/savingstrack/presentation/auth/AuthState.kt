package com.dmytroherez.savingstrack.presentation.auth

import com.dmytroherez.savingstrack.core.presentation.BaseUiState

data class AuthState(
    val email: String = "",
    val password: String = "",
    val mode: AuthMode = AuthMode.LOGIN,
    val isLoading: Boolean = false
): BaseUiState

enum class AuthMode {
    LOGIN, REGISTER
}

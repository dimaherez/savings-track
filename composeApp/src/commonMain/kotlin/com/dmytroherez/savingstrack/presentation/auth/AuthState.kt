package com.dmytroherez.savingstrack.presentation.auth

import com.dmytroherez.savingstrack.core.presentation.BaseUiState

data class AuthState(
    val email: String = "",
    val password: String = ""
): BaseUiState

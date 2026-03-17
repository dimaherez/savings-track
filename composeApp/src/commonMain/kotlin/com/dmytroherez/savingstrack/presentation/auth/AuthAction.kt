package com.dmytroherez.savingstrack.presentation.auth

import com.dmytroherez.savingstrack.domain.UserModel

sealed interface AuthAction {
    data class OnEmailInputChanged(val emailText: String): AuthAction
    data class OnPasswordInputChanged(val passwordText: String): AuthAction
    data object SubmitLogin: AuthAction
    data class Register(val user: UserModel): AuthAction
    data class ForgotPassword(val user: UserModel): AuthAction
    data class GoogleSignIn(val user: UserModel): AuthAction
}
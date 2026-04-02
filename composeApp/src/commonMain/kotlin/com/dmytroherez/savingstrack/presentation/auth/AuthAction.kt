package com.dmytroherez.savingstrack.presentation.auth

import com.dmytroherez.savingstrack.core.presentation.BaseAction
import com.dmytroherez.savingstrack.domain.UserModel

sealed interface AuthAction : BaseAction {
    data class OnEmailInputChanged(val emailText: String): AuthAction
    data class OnPasswordInputChanged(val passwordText: String): AuthAction
    data object SubmitAuthAction: AuthAction
    data object ToggleMode : AuthAction
    data class Register(val user: UserModel): AuthAction
    data class ForgotPassword(val user: UserModel): AuthAction
    data class GoogleSignIn(val user: UserModel): AuthAction
    data class AppleSignIn(val user: UserModel): AuthAction
}
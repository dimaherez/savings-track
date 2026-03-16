package com.dmytroherez.savingstrack.auth.presentation.auth

import com.dmytroherez.savingstrack.core.presentation.BaseViewModel

class AuthViewModel : BaseViewModel<AuthState, AuthEvent>(
    AuthState()
) {
    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.OnEmailInputChanged -> {
                updateState {
                    it.copy(email = action.emailText)
                }
            }

            is AuthAction.OnPasswordInputChanged -> {
                updateState {
                    it.copy(password = action.passwordText)
                }
            }

            is AuthAction.SubmitLogin -> {
                sendEvent(
                    AuthEvent.LoginSuccess
                )
            }

            is AuthAction.Register -> Unit
            is AuthAction.ForgotPassword -> Unit
            is AuthAction.GoogleSignIn -> Unit
        }
    }

}
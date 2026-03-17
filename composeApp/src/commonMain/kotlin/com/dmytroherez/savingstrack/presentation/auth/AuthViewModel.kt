package com.dmytroherez.savingstrack.presentation.auth

import androidx.lifecycle.viewModelScope
import com.dmytroherez.savingstrack.core.datastore.DataStoreRepo
import com.dmytroherez.savingstrack.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class AuthViewModel(
    private val dataStoreRepo: DataStoreRepo
) : BaseViewModel<AuthState, AuthEvent>(
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
                viewModelScope.launch {
                    dataStoreRepo.setIsLoggedIn()
                    sendEvent(
                        AuthEvent.LoginSuccess
                    )
                }
            }

            is AuthAction.Register -> Unit
            is AuthAction.ForgotPassword -> Unit
            is AuthAction.GoogleSignIn -> Unit
        }
    }

}
package com.dmytroherez.savingstrack.presentation.auth

import androidx.lifecycle.viewModelScope
import com.dmytroherez.savingstrack.core.datastore.DataStoreRepo
import com.dmytroherez.savingstrack.core.presentation.BaseViewModel
import com.dmytroherez.savingstrack.core.presentation.UiText
import com.dmytroherez.savingstrack.domain.usecase.LoginUC
import com.dmytroherez.savingstrack.domain.usecase.RegisterUC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class AuthViewModel(
    private val dataStoreRepo: DataStoreRepo,
    private val registerUC: RegisterUC,
    private val loginUC: LoginUC
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
                viewModelScope.launch(Dispatchers.IO) {
                    loginUC(
                        email = state.value.email,
                        pass = state.value.password
                    )
                        .onSuccess {
                            dataStoreRepo.setIsLoggedIn()
                            sendEvent(
                                AuthEvent.LoginSuccess
                            )
                        }
                        .onFailure {
                            sendEvent(
                                AuthEvent.ShowErrorToast(
                                    UiText.DynamicString(it.message.toString())
                                )
                            )
                        }
                }
            }

            is AuthAction.Register -> {
                viewModelScope.launch(Dispatchers.IO) {
                    registerUC(
                        email = state.value.email,
                        pass = state.value.password
                    )
                        .onSuccess {
                            dataStoreRepo.setIsLoggedIn()
                            sendEvent(
                                AuthEvent.LoginSuccess
                            )
                        }
                        .onFailure {
                            sendEvent(
                                AuthEvent.ShowErrorToast(
                                    UiText.DynamicString(it.message.toString())
                                )
                            )
                        }
                }
            }
            is AuthAction.ForgotPassword -> Unit
            is AuthAction.GoogleSignIn -> Unit
        }
    }

}
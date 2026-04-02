package com.dmytroherez.savingstrack.presentation.auth

import androidx.lifecycle.viewModelScope
import com.dmytroherez.savingstrack.core.datastore.DataStoreRepo
import com.dmytroherez.savingstrack.core.presentation.BaseViewModel
import com.dmytroherez.savingstrack.core.presentation.UiText.*
import com.dmytroherez.savingstrack.domain.usecase.auth.LoginUC
import com.dmytroherez.savingstrack.domain.usecase.auth.RegisterUC
import com.dmytroherez.savingstrack.presentation.auth.AuthEvent.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class AuthViewModel(
    private val dataStoreRepo: DataStoreRepo,
    private val registerUC: RegisterUC,
    private val loginUC: LoginUC
) : BaseViewModel<AuthState, AuthEvent, AuthAction>(
    AuthState()
) {
    override fun onAction(action: AuthAction) {
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

            is AuthAction.SubmitAuthAction -> {
                viewModelScope.launch(Dispatchers.IO) {
                    when(state.value.mode) {
                        AuthMode.LOGIN -> {
                            login()
                        }
                        AuthMode.REGISTER -> {
                            register()
                        }
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
                                RegisterSuccess
                            )
                        }
                        .onFailure {
                            sendEvent(
                                ShowErrorToast(
                                    DynamicString(it.message.toString())
                                )
                            )
                        }
                }
            }

            AuthAction.ToggleMode -> updateState {
                it.copy(mode = AuthMode.entries.first { mode -> it.mode != mode})
            }

            is AuthAction.ForgotPassword -> Unit
            is AuthAction.GoogleSignIn -> Unit
            is AuthAction.AppleSignIn -> TODO()
        }
    }

    private suspend fun login() {
        setIsLoading(true)
        loginUC(
            email = state.value.email,
            pass = state.value.password
        )
            .onSuccess {
                dataStoreRepo.setIsLoggedIn()
                sendEvent(
                    RegisterSuccess
                )
                setIsLoading(false)
            }
            .onFailure {
                sendEvent(
                    ShowErrorToast(
                        DynamicString(it.message.toString())
                    )
                )
                setIsLoading(false)
            }
    }

    private suspend fun register() {
        setIsLoading(true)
        registerUC(
            email = state.value.email,
            pass = state.value.password
        )
            .onSuccess {
                dataStoreRepo.setIsLoggedIn()
                sendEvent(
                    RegisterSuccess
                )
                setIsLoading(false)
            }
            .onFailure {
                sendEvent(
                    ShowErrorToast(
                        DynamicString(it.message.toString())
                    )
                )
                setIsLoading(false)
            }
    }

    private fun setIsLoading(isLoading: Boolean) {
        updateState { it.copy(isLoading = isLoading) }
    }

}
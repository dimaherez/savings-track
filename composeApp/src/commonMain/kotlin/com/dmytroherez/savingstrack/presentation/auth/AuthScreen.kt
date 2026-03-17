package com.dmytroherez.savingstrack.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dmytroherez.savingstrack.MainScreen
import com.dmytroherez.savingstrack.core.presentation.components.buttons.ButtonPrimary
import com.dmytroherez.savingstrack.core.presentation.components.inputs.TextInput
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

data object AuthScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<AuthViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val navigator = LocalNavigator.currentOrThrow
        val lifecycleOwner = LocalLifecycleOwner.current

        LaunchedEffect(lifecycleOwner.lifecycle) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        AuthEvent.LoginSuccess -> {
                            navigator.replaceAll(MainScreen) // to clear backstack
                        }
                    }
                }
            }
        }

        AuthScreenContent(
            state = state,
            onAction = viewModel::onAction
        )
    }
}

@Composable
fun AuthScreenContent(
    state: AuthState,
    onAction: (AuthAction) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Welcome",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(Modifier.height(32.dp))

        CredentialInputs(
            state = state,
            onAction = onAction,
            focusManager = focusManager
        )

        Spacer(Modifier.height(16.dp))

        ButtonPrimary(
            modifier = Modifier.fillMaxWidth(),
            text = "Login",
            onClick = { onAction(AuthAction.SubmitLogin) }
        )
    }
}

@Composable
private fun CredentialInputs(
    state: AuthState,
    onAction: (AuthAction) -> Unit,
    focusManager: FocusManager
) {
    TextInput(
        modifier = Modifier.fillMaxWidth(),
        hint = "Email",
        value = state.email,
        error = null,
        onValueChange = { onAction(AuthAction.OnEmailInputChanged(it)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
    )

    Spacer(Modifier.height(8.dp))

    TextInput(
        modifier = Modifier.fillMaxWidth(),
        hint = "Password",
        value = state.password,
        onValueChange = { onAction(AuthAction.OnPasswordInputChanged(it)) },
        visualTransformation = PasswordVisualTransformation(),
        error = null,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onAction(AuthAction.SubmitLogin)
            }
        )
    )
}


@Preview
@Composable
private fun AuthScreenPreview() {
    AuthScreenContent(
        state = AuthState(

        )
    )
}
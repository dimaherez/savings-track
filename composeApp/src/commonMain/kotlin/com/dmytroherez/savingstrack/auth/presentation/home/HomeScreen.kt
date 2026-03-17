package com.dmytroherez.savingstrack.auth.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import org.koin.compose.viewmodel.koinViewModel

data object HomeScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<HomeViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        HomeScreenContent(
            state = state,
            onAction = viewModel::onAction
        )
    }
}

@Composable
private fun HomeScreenContent (
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Home",
            style = MaterialTheme.typography.headlineLarge
        )
    }
}
package com.dmytroherez.savingstrack.presentation.fiat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.koin.compose.viewmodel.koinViewModel

object FiatTab: Tab {
    override val options: TabOptions
        @Composable
        get() = remember {
            TabOptions(
                index = 0u,
                title = "Fiat",
                icon = null
            )
        }

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<FiatViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        FiatScreenContent(
            state = state,
            onAction = viewModel::onAction
        )
    }
}

@Composable
private fun FiatScreenContent(
    state: FiatState,
    onAction: (FiatAction) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Fiat",
            style = MaterialTheme.typography.headlineLarge
        )
    }
}
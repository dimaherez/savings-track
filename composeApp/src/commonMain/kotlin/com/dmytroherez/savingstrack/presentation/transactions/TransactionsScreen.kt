package com.dmytroherez.savingstrack.presentation.transactions

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import cafe.adriel.voyager.core.screen.Screen
import com.dmytroherez.savingstrack.core.utils.formatAsFiat
import multiplatform.network.cmptoast.showToast
import org.koin.compose.viewmodel.koinViewModel

data class TransactionsScreen(private val currency: String) : Screen {
    @Composable
    override fun Content() {
        val lifecycleOwner = LocalLifecycleOwner.current

        val viewModel = koinViewModel<TransactionsViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(lifecycleOwner.lifecycle) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is TransactionsEvent.ShowToast -> {
                            showToast(event.message.asStringSuspend())
                        }
                    }
                }
            }
        }


        viewModel.getTransactions(currency)

        TransactionsScreenContent(
            state = state,
            onAction = viewModel::onAction
        )
    }
}

@Composable
fun TransactionsScreenContent(
    state: TransactionsState,
    onAction: (TransactionsAction) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Total holding",
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = formatAsFiat(state.totalAmount, state.currency),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.transactions.forEach { transactionItem ->
                item {
                    Column(
                        modifier = Modifier
                            .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(24.dp))
                            .clip(RoundedCornerShape(24.dp))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(transactionItem.createdAt.toString())
                            Text(formatAsFiat(transactionItem.amount, transactionItem.currency, showSign = true))
                        }

                        transactionItem.description?.let { desc ->
                            Text(text = desc )
                        }
                    }
                }
            }
        }
    }
}
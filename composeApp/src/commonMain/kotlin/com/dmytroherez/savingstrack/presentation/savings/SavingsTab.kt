package com.dmytroherez.savingstrack.presentation.savings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.dmytroherez.savingstrack.LocalRootNavigator
import com.dmytroherez.savingstrack.core.presentation.components.PreviewWithTheme
import com.dmytroherez.savingstrack.core.presentation.components.buttons.ButtonRound
import com.dmytroherez.savingstrack.core.presentation.components.listitem.CurrencyTotalItemContent
import com.dmytroherez.savingstrack.shared.dto.transactions.CurrencyTotal
import com.dmytroherez.savingstrack.shared.dto.transactions.SavingCategory
import com.dmytroherez.savingstrack.presentation.savings.addtransaction.AddTransactionBottomSheet
import com.dmytroherez.savingstrack.presentation.transactions.TransactionsScreen
import kotlinx.coroutines.launch
import multiplatform.network.cmptoast.showToast
import org.koin.compose.viewmodel.koinViewModel

object SavingsTab : Tab {
    override val options: TabOptions
        @Composable
        get() = remember {
            TabOptions(
                index = 0u,
                title = "Savings",
                icon = null
            )
        }

    @Composable
    override fun Content() {
        val rootNavigator = LocalRootNavigator.current
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        val coroutineScope = rememberCoroutineScope()
        val lifecycleOwner = LocalLifecycleOwner.current

        val viewModel = koinViewModel<SavingsViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val tabs = SavingCategory.entries.take(2)
        val pagerState = rememberPagerState { tabs.size }

        LaunchedEffect(lifecycleOwner.lifecycle) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is SavingsEvent.ShowToast -> {
                            showToast(event.message.asStringSuspend())
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            SecondaryTabRow(
                selectedTabIndex = pagerState.currentPage
            ) {
                tabs.forEach { tabEntry ->
                    Tab(
                        selected = pagerState.currentPage == tabEntry.ordinal,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(tabEntry.ordinal)
                            }
                        },
                        text = { Text(text = tabEntry.name) }
                    )
                }
            }

            HorizontalPager(
                state = pagerState
            ) {
                SavingsScreenContent(
                    state = state,
                    pagerState = pagerState,
                    onAction = viewModel::onAction,
                    onNavigateToCurrencyTransactions = { currency ->
                        rootNavigator?.push(TransactionsScreen(currency))
                    },
                    showAddTransactionDialog = {
                        bottomSheetNavigator.show(AddTransactionBottomSheet)
                    }
                )
            }
        }

    }
}

@Composable
private fun SavingsScreenContent(
    state: SavingsState,
    pagerState: PagerState,
    onAction: (SavingsAction) -> Unit = {},
    onNavigateToCurrencyTransactions: (currency: String) -> Unit = {},
    showAddTransactionDialog: () -> Unit = {}
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
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
                        text = "$1596",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (state.savings?.categories[SavingCategory.entries[pagerState.currentPage]] ?: emptyList())
                    .forEach { currencyTotal ->
                        item(
                            contentType = CurrencyTotal,
                            key = currencyTotal.currency
                        ) {
                            CurrencyTotalItemContent(
                                currencyTotal,
                                onViewAllClick = { currency ->
                                    onNavigateToCurrencyTransactions(
                                        currency
                                    )
                                }
                            )
                        }
                    }
            }
        }

        if (state.isSavingsListLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        ButtonRound(
            modifier = Modifier
                .padding(end = 36.dp, bottom = 36.dp)
                .align(Alignment.BottomEnd),
            imageVector = Icons.Filled.Add,
            onCLick = showAddTransactionDialog
        )
    }
}

@Preview
@Composable
private fun FiatScreenContentPreview() = PreviewWithTheme {
    SavingsScreenContent(
        state = SavingsState(),
        pagerState = rememberPagerState { 0 }
    )
}
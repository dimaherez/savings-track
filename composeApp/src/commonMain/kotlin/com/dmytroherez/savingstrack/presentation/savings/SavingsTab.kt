package com.dmytroherez.savingstrack.presentation.savings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.dmytroherez.savingstrack.core.presentation.components.PreviewWithTheme
import com.dmytroherez.savingstrack.core.presentation.components.buttons.ButtonPrimary
import com.dmytroherez.savingstrack.core.utils.formatAsFiat
import com.dmytroherez.savingstrack.dto.transactions.CurrencyTotal
import com.dmytroherez.savingstrack.dto.transactions.PostTransactionRequest
import com.dmytroherez.savingstrack.dto.transactions.SavingCategory
import kotlinx.coroutines.launch
import multiplatform.network.cmptoast.showToast
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

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
            TabRow(
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
                FiatScreenContent(
                    state = state,
                    pagerState = pagerState,
                    onAction = viewModel::onAction
                )
            }
        }

    }
}

@Composable
private fun FiatScreenContent(
    state: SavingsState,
    pagerState: PagerState,
    onAction: (SavingsAction) -> Unit = {}
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

//            Text(
//                modifier = Modifier
//                    .weight(1f)
//                    .clip(RoundedCornerShape(24.dp))
//                    .fillMaxWidth()
//                    .background(MaterialTheme.colorScheme.secondaryContainer),
//                textAlign = TextAlign.Center,
//                text = "*Chart*",
//                style = MaterialTheme.typography.headlineLarge
//            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .weight(1f)
            ) {
                listItems(
                    currencyTotals = state.savings?.categories[SavingCategory.entries[pagerState.currentPage]] ?: emptyList()
                )
            }
        }

        if (state.isSavingsListLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        AddSavingDialog(
            isVisible = state.showAddSavingDialog,
            onAddClick = { request -> onAction(SavingsAction.PostSaving(request)) },
            onDismissRequest = { onAction(SavingsAction.ToggleAddSavingDialog) }
        )

        TextButton(
            modifier = Modifier
                .padding(end = 36.dp, bottom = 36.dp)
                .align(Alignment.BottomEnd)
                .clip(CircleShape)
                .size(48.dp)
                .background(color = MaterialTheme.colorScheme.primary),
            onClick = {
                onAction(SavingsAction.ToggleAddSavingDialog)
            }
        ) {
            Text(
                text = "+",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
private fun LazyListScope.listItems(
    currencyTotals: List<CurrencyTotal>
) {
    currencyTotals
        .forEach { savingItem ->
            item {
                var dropdownExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded,
                    onExpandedChange = { dropdownExpanded = !dropdownExpanded }
                ) {
                    Row(
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(savingItem.currency)
                        Text(formatAsFiat(savingItem.totalAmount, savingItem.currency))
                    }
                    ExposedDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        savingItem.recentTransactions.forEach { transaction ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(transaction.createdAt.toString())
                                        Text(formatAsFiat(transaction.amount, savingItem.currency, showSign = true))
                                    }
                                },
                                onClick = {}
                            )
                        }
                        if (savingItem.hasMoreTransactions) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "View all",
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                },
                                onClick = {}
                            )
                        }
                    }
                }
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddSavingDialog(
    isVisible: Boolean,
    onAddClick: (PostTransactionRequest) -> Unit,
    onDismissRequest: () -> Unit
) {
    if (isVisible.not()) return

    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCurrency by remember { mutableStateOf("") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    val availableCurrencies = listOf("USD", "EUR", "GBP", "UAH", "JPY")

    Box(
        modifier = Modifier.fillMaxSize()
            .clickable(
                onClick = onDismissRequest
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = dropdownExpanded,
                onExpandedChange = { dropdownExpanded = !dropdownExpanded }
            ) {
                OutlinedTextField(
                    value = selectedCurrency,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Currency") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(dropdownExpanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    availableCurrencies.forEach { currency ->
                        DropdownMenuItem(
                            text = { Text(currency) },
                            onClick = {
                                selectedCurrency = currency
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }

            // Amount Input
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth()
            )

            // Description Input (Optional)
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            ButtonPrimary(
                text = "Save",
                onClick = {
                    onAddClick(
                        PostTransactionRequest(
                            currency = selectedCurrency,
                            amount = amount.toDoubleOrNull() ?: 0.0,
                            description = description,
                            category = SavingCategory.FIAT
                        )
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun FiatScreenContentPreview() = PreviewWithTheme {
    FiatScreenContent(
        state = SavingsState(),
        pagerState = rememberPagerState { 0 }
    )
}
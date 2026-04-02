package com.dmytroherez.savingstrack.presentation.savings.addtransaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.dmytroherez.savingstrack.core.presentation.components.AppDropdown
import com.dmytroherez.savingstrack.shared.dto.transactions.SavingCategory
import multiplatform.network.cmptoast.showToast
import org.koin.compose.viewmodel.koinViewModel

object AddTransactionBottomSheet : Screen {
    @Composable
    override fun Content() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        val lifecycleOwner = LocalLifecycleOwner.current

        val viewModel = koinViewModel<AddTransactionViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(lifecycleOwner.lifecycle) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is AddTransactionEvent.ShowToast -> {
                            showToast(event.message.asStringSuspend())
                        }

                        AddTransactionEvent.CloseBottomSheet -> bottomSheetNavigator.hide()
                    }
                }
            }
        }

        AddTransactionBottomSheetContent(
            state = state,
            onAction = viewModel::onAction
        )
    }
}

@Composable
private fun AddTransactionBottomSheetContent(
    state: AddTransactionState,
    onAction: (AddTransactionAction) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(24.dp)
            .imePadding()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Add Transaction",
            style = MaterialTheme.typography.headlineSmall
        )

        AppDropdown(
            label = "Goal",
            options = state.availableGoals,
            selectedOption = state.goal,
            onOptionSelected = { onAction(AddTransactionAction.OnSetGoal(it)) },
            displayText = { it?.title ?: "null" }
        )

        AppDropdown(
            label = "Category",
            options = SavingCategory.entries,
            selectedOption = state.category,
            onOptionSelected = { onAction(AddTransactionAction.OnCategoryChange(it)) },
            displayText = { it.name }
        )

        AppDropdown(
            label = "Currency",
            options = state.availableCurrencies,
            selectedOption = state.currency,
            onOptionSelected = { onAction(AddTransactionAction.OnCurrencyChange(it)) }
        )

        OutlinedTextField(
            value = state.amount,
            onValueChange = { onAction(AddTransactionAction.OnAmountChange(it)) },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.description,
            onValueChange = { onAction(AddTransactionAction.OnDescriptionChange(it)) },
            label = { Text("Description (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Standardized Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { onAction(AddTransactionAction.OnCancelClick) }) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                enabled = state.isLoading.not(),
                onClick = { onAction(AddTransactionAction.OnSaveClick) }
            ) {
                Text("Save")
            }
        }
    }
}
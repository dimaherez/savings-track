package com.dmytroherez.savingstrack.presentation.home.addgoal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import com.dmytroherez.savingstrack.presentation.savings.addtransaction.AddTransactionAction
import multiplatform.network.cmptoast.showToast
import org.koin.compose.viewmodel.koinViewModel

object AddGoalBottomSheet : Screen {
    @Composable
    override fun Content() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        val lifecycleOwner = LocalLifecycleOwner.current

        val viewModel = koinViewModel<AddGoalViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(lifecycleOwner.lifecycle) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is AddGoalEvent.ShowToast -> {
                            showToast(event.message.asStringSuspend())
                        }

                        AddGoalEvent.Dismiss -> bottomSheetNavigator.hide()
                    }
                }
            }
        }

        AddGoalBottomSheetContent(
            state = state,
            onAction = viewModel::onAction
        )
    }
}

@Composable
private fun AddGoalBottomSheetContent(
    state: AddGoalState,
    onAction: (AddGoalAction) -> Unit
) {
    val datePickerState = rememberDatePickerState()

    if (state.showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { onAction(AddGoalAction.ToggleDatePicker) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onAction(AddGoalAction.OnDateChange(datePickerState.selectedDateMillis))
                    }) {
                    Text("Confirm")
                }
            },
            dismissButton = { TextButton(onClick = { onAction(AddGoalAction.ToggleDatePicker) }) { Text("Cancel") } }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .imePadding()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Create New Goal",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = state.title,
            onValueChange = { onAction(AddGoalAction.OnTitleChange(it)) },
            label = { Text("Goal Title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.amount,
            onValueChange = { onAction(AddGoalAction.OnAmountChange(it)) },
            label = { Text("Target Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        AppDropdown(
            label = "Currency",
            options = state.availableCurrencies,
            selectedOption = state.currency,
            onOptionSelected = { onAction(AddGoalAction.OnCurrencyChange(it)) }
        )

        OutlinedTextField(
            value = state.selectedDate?.toString() ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Deadline (Optional)") },
            trailingIcon = {
                IconButton(onClick = { onAction(AddGoalAction.ToggleDatePicker) }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { onAction(AddGoalAction.Dismiss) }) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                enabled = state.isLoading.not(),
                onClick = { onAction(AddGoalAction.AddGoal) }
            ) {
                Text("Save")
            }
        }
    }
}
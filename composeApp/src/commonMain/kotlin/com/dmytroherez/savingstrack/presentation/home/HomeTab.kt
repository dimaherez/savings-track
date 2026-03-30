package com.dmytroherez.savingstrack.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.dmytroherez.savingstrack.core.utils.formatAsFiat
import com.dmytroherez.savingstrack.dto.goals.CreateGoalRequest
import com.dmytroherez.savingstrack.dto.goals.GoalItem
import com.dmytroherez.savingstrack.presentation.savings.SavingsAction
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Instant

object HomeTab: Tab {
    override val options: TabOptions
        @Composable
        get() = remember {
            TabOptions(
                index = 0u,
                title = "Home",
                icon = null
            )
        }

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
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            Heading()

            LazyColumn {
                listItems(
                    goals = state.goals,
                    onViewAllClick = {}
                )
            }
        }

        if (state.showAddGoalDialog) {
            CreateGoalDialog(
                onDismiss = { onAction(HomeAction.ToggleAddGoalDialog) },
                onSave = { request -> onAction(HomeAction.AddGoal(request)) }
            )
        }

        TextButton(
            modifier = Modifier
                .padding(end = 36.dp, bottom = 36.dp)
                .align(Alignment.BottomEnd)
                .clip(CircleShape)
                .size(48.dp)
                .background(color = MaterialTheme.colorScheme.primary),
            onClick = {
                onAction(HomeAction.ToggleAddGoalDialog)
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

@Composable
private fun Heading() {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Total saved",
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = "$1596",
                style = MaterialTheme.typography.headlineMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    modifier = Modifier.width(IntrinsicSize.Max)
                ) {
                    Text(text = "Fiat:")
                    Text(text = "Crypto:")
                }

                Column {
                    Text(text = "$1000")
                    Text(text = "$596")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun LazyListScope.listItems(
    goals: List<GoalItem>,
    onViewAllClick: (goalId: Int) -> Unit
) {
    goals
        .forEach { goalItem ->
            item {
                var dropdownExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded,
                    onExpandedChange = { dropdownExpanded = !dropdownExpanded }
                ) {
                    Column(
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${goalItem.title} - ${formatAsFiat(goalItem.targetAmount, goalItem.currency)}")
                            Text("Until: ${goalItem.deadline}")
                        }

                        GoalProgressBar(
                            progress = goalItem.progressPercentage / 100f,
                            amount = goalItem.currentAmount,
                            currency = goalItem.currency
                        )
                    }
                    ExposedDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        goalItem.recentTransactions.forEach { transaction ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(transaction.createdAt.toString())
                                        Text(formatAsFiat(transaction.amount, goalItem.currency, showSign = true))
                                    }
                                },
                                onClick = {}
                            )
                        }
                    }
                }
            }
        }
}

@Composable
fun GoalProgressBar(
    progress: Float,
    amount: Double,
    currency: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        val totalWidth = constraints.maxWidth
                        var xPosition = (totalWidth * progress).toInt() - (placeable.width / 2)
                        xPosition = xPosition.coerceIn(0, totalWidth - placeable.width)
                        layout(totalWidth, placeable.height) {
                            placeable.placeRelative(x = xPosition, y = 0)
                        }
                    }
            ) {
                Text(
                    text = "${formatAsFiat(amount, currency)}\n(${(progress * 100).toInt()}%)",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Current Progress",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp).offset(y = (-4).dp)
                )
            }
        }

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(50)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGoalDialog(
    availableCurrencies: List<String> = listOf("USD", "EUR", "UAH"), // Standard defaults
    onDismiss: () -> Unit,
    onSave: (CreateGoalRequest) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedCurrency by remember { mutableStateOf(availableCurrencies.first()) }

    // Date Picker State
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Dropdown State
    var expandedCurrency by remember { mutableStateOf(false) }

    val selectedDateString = datePickerState.selectedDateMillis?.let { millis ->
        Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
            .toString() // Returns ISO-8601 format like "2026-12-31"
    } ?: ""

    // The Date Picker Overlay
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }


    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Create New Goal",
                    style = MaterialTheme.typography.headlineSmall
                )

                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Goal Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Target Amount Input
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Target Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Currency Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedCurrency,
                    onExpandedChange = { expandedCurrency = !expandedCurrency }
                ) {
                    OutlinedTextField(
                        value = selectedCurrency,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Currency") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedCurrency) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCurrency,
                        onDismissRequest = { expandedCurrency = false }
                    ) {
                        availableCurrencies.forEach { currency ->
                            DropdownMenuItem(
                                text = { Text(currency) },
                                onClick = {
                                    selectedCurrency = currency
                                    expandedCurrency = false
                                }
                            )
                        }
                    }
                }

                // Deadline Input (Triggers Date Picker)
                OutlinedTextField(
                    value = selectedDateString,
                    onValueChange = {},
                    readOnly = true, // Prevent manual typing
                    label = { Text("Deadline (Optional)") },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        enabled = title.isNotBlank() && amount.toDoubleOrNull() != null,
                        onClick = {
                            val targetAmount = amount.toDouble()
                            val deadlineDate = datePickerState.selectedDateMillis?.let { millis ->
                                Instant.fromEpochMilliseconds(millis)
                                    .toLocalDateTime(TimeZone.currentSystemDefault())
                                    .date
                            }

                            onSave(
                                CreateGoalRequest(
                                    title = title,
                                    targetAmount = targetAmount,
                                    currency = selectedCurrency,
                                    deadline = deadlineDate
                                )
                            )
                        }
                    ) {
                        Text("Save Goal")
                    }
                }
            }
        }
    }
}
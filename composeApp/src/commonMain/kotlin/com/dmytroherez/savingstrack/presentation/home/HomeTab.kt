package com.dmytroherez.savingstrack.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.dmytroherez.savingstrack.core.utils.formatAsFiat
import com.dmytroherez.savingstrack.dto.goals.GoalItem
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextAlign

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
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Heading()

        LazyColumn {
            listItems(
                goals = state.goals,
                onViewAllClick = {}
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

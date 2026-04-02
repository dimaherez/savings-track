package com.dmytroherez.savingstrack.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.dmytroherez.savingstrack.core.presentation.components.buttons.ButtonRound
import com.dmytroherez.savingstrack.core.presentation.components.listitem.GoalItemContent
import com.dmytroherez.savingstrack.shared.dto.goals.GoalItem
import com.dmytroherez.savingstrack.presentation.home.addgoal.AddGoalBottomSheet
import kotlinx.coroutines.flow.collectLatest
import multiplatform.network.cmptoast.showToast
import org.koin.compose.viewmodel.koinViewModel

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

        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val lifecycleOwner = LocalLifecycleOwner.current

        LaunchedEffect(lifecycleOwner.lifecycle) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collectLatest { event ->
                    when(event) {
                        is HomeEvent.ShowToast -> showToast(event.message.asStringSuspend())
                    }
                }
            }
        }

        HomeScreenContent(
            state = state,
            onAction = viewModel::onAction,
            onAddGoalClick = {
                bottomSheetNavigator.show(AddGoalBottomSheet)
            }
        )
    }
}

@Composable
private fun HomeScreenContent (
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    onAddGoalClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Heading()

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.goals.forEach { goalItem ->
                    item(
                        contentType = GoalItem,
                        key = goalItem.id
                    ) {
                        GoalItemContent(
                            goalItem,
                            onViewAllClick = {}
                        )
                    }
                }
            }
        }

        if (state.goalsLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        ButtonRound(
            modifier = Modifier
                .padding(end = 36.dp, bottom = 36.dp)
                .align(Alignment.BottomEnd),
            imageVector = Icons.Filled.Add,
            onCLick = onAddGoalClick
        )
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
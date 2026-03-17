package com.dmytroherez.savingstrack.presentation.fiat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.dmytroherez.savingstrack.core.presentation.components.PreviewWithTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
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
    onAction: (FiatAction) -> Unit = {}
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
                        text = "Total saved",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = "$1596",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            Text(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                textAlign = TextAlign.Center,
                text = "*Chart*",
                style = MaterialTheme.typography.headlineLarge
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1.5f)
                    .clip(RoundedCornerShape(24.dp))
                    .weight(1f)
            ) {
                items(4) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Currency$it")
                        Text("Value$it")
                    }
                }
            }
        }

        Text(
            modifier = Modifier
                .padding(end = 36.dp, bottom = 36.dp)
                .align(Alignment.BottomEnd)
                .clip(CircleShape)
                .size(48.dp)
                .background(color = MaterialTheme.colorScheme.primary),
            text = "+",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            textAlign = TextAlign.Center,
        )

    }
}

@Preview
@Composable
private fun FiatScreenContentPreview() = PreviewWithTheme {
    FiatScreenContent(
        state = FiatState()
    )
}
package com.dmytroherez.savingstrack.core.presentation.components.listitem

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.dmytroherez.savingstrack.core.presentation.Constants.ROUNDED_SHAPE_DEFAULT
import com.dmytroherez.savingstrack.core.presentation.Extensions.toDisplayDateString
import com.dmytroherez.savingstrack.core.presentation.components.dropdown.TransactionsDropdownMenu
import com.dmytroherez.savingstrack.core.utils.formatAsFiat
import com.dmytroherez.savingstrack.dto.goals.GoalItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalItemContent(
    goalItem: GoalItem,
    onViewAllClick: (Int) -> Unit
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ROUNDED_SHAPE_DEFAULT)
            .border(width = 2.dp, color = MaterialTheme.colorScheme.primary, shape = ROUNDED_SHAPE_DEFAULT),
        expanded = dropdownExpanded,
        onExpandedChange = { dropdownExpanded = !dropdownExpanded }
    ) {
        Box {
            LinearProgressIndicator(
                modifier = Modifier.matchParentSize(),
                strokeCap = StrokeCap.Square,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                trackColor = Color.Transparent,
                progress = {
                    goalItem.progress
                }
            )

            Column(
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${goalItem.title} - ${formatAsFiat(goalItem.targetAmount, goalItem.currency)}")
                    Text("Until: ${goalItem.deadline?.toDisplayDateString()}")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "${goalItem.progress * 100}%",
                    )
                }
            }
        }

        TransactionsDropdownMenu(
            expanded = dropdownExpanded,
            transactions = goalItem.recentTransactions,
            hasMoreTransactions = goalItem.hasMoreTransactions,
            onDismissRequest = { dropdownExpanded = false },
            onViewAllClick = { onViewAllClick(goalItem.id) },
        )
    }
}
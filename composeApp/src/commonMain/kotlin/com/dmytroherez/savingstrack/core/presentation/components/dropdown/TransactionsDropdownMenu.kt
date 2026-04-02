package com.dmytroherez.savingstrack.core.presentation.components.dropdown

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.dmytroherez.savingstrack.core.presentation.components.listitem.TransactionDropdownItemContent
import com.dmytroherez.savingstrack.dto.transactions.TransactionItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenuBoxScope.TransactionsDropdownMenu(
    expanded: Boolean,
    transactions: List<TransactionItem>,
    hasMoreTransactions: Boolean,
    onDismissRequest: () -> Unit,
    onViewAllClick: () -> Unit
) {
    ExposedDropdownMenu(
        expanded = expanded && transactions.isNotEmpty(),
        onDismissRequest = { onDismissRequest() }
    ) {
        transactions.forEach { transaction ->
            DropdownMenuItem(
                text = { TransactionDropdownItemContent(transaction) },
                onClick = {}
            )
        }
        if (hasMoreTransactions) {
            DropdownMenuItem(
                text = {
                    Text(
                        modifier = Modifier.fillMaxSize(),
                        text = "View all transactions",
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                },
                onClick = { onViewAllClick() }
            )
        }
    }
}
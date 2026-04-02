package com.dmytroherez.savingstrack.core.presentation.components.listitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dmytroherez.savingstrack.core.presentation.components.dropdown.TransactionsDropdownMenu
import com.dmytroherez.savingstrack.core.utils.formatAsFiat
import com.dmytroherez.savingstrack.dto.transactions.CurrencyTotal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyTotalItemContent(
    currencyTotal: CurrencyTotal,
    onViewAllClick: (String) -> Unit
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = dropdownExpanded,
        onExpandedChange = { dropdownExpanded = !dropdownExpanded }
    ) {
        Row(
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(currencyTotal.currency)
            Text(formatAsFiat(currencyTotal.totalAmount, currencyTotal.currency))
        }

        TransactionsDropdownMenu(
            expanded = dropdownExpanded,
            transactions = currencyTotal.recentTransactions,
            hasMoreTransactions = currencyTotal.hasMoreTransactions,
            onDismissRequest = { dropdownExpanded = false },
            onViewAllClick = { onViewAllClick(currencyTotal.currency) },
        )
    }
}
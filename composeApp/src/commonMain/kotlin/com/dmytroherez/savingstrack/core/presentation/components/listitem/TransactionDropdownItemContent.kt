package com.dmytroherez.savingstrack.core.presentation.components.listitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dmytroherez.savingstrack.core.presentation.Extensions.toDisplayDateTimeString
import com.dmytroherez.savingstrack.core.utils.formatAsFiat
import com.dmytroherez.savingstrack.dto.transactions.TransactionItem

@Composable
fun TransactionDropdownItemContent(
    transactionItem: TransactionItem
) = with(transactionItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(createdAt.toDisplayDateTimeString())
        Text(formatAsFiat(amount, currency, showSign = true))
    }
}
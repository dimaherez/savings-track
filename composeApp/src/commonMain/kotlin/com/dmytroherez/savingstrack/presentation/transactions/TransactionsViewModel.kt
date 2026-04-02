package com.dmytroherez.savingstrack.presentation.transactions

import androidx.lifecycle.viewModelScope
import com.dmytroherez.savingstrack.core.presentation.BaseViewModel
import com.dmytroherez.savingstrack.core.presentation.UiText
import com.dmytroherez.savingstrack.domain.usecase.savings.GetTransactionsByCurrencyUC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class TransactionsViewModel(
    private val getTransactionsByCurrencyUC: GetTransactionsByCurrencyUC
) : BaseViewModel<TransactionsState, TransactionsEvent, TransactionsAction>(
    TransactionsState()
) {

    override fun onAction(action: TransactionsAction) {
        when(action) {
            TransactionsAction.SomeAction -> Unit
        }
    }

    fun getTransactions(currency: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { it.copy(
                isLoading = true,
                currency = currency
            ) }
            getTransactionsByCurrencyUC(currency)
                .onSuccess { result ->
                    updateState {
                        it.copy(
                            isLoading = true,
                            transactions = result.transactions,
                            totalAmount = result.totalAmount
                        )
                    }
                }
                .onFailure { err ->
                    updateState { it.copy(isLoading = false) }
                    sendEvent(TransactionsEvent.ShowToast(UiText.DynamicString(err.message)))
                }
        }
    }

}
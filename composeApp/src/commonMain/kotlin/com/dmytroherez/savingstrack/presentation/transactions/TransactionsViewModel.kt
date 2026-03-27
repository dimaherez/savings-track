package com.dmytroherez.savingstrack.presentation.transactions

import androidx.lifecycle.viewModelScope
import com.dmytroherez.savingstrack.core.presentation.BaseViewModel
import com.dmytroherez.savingstrack.core.presentation.UiText
import com.dmytroherez.savingstrack.domain.usecase.savings.GetTransactionsUC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class TransactionsViewModel(
    private val getTransactionsUC: GetTransactionsUC
) : BaseViewModel<TransactionsState, TransactionsEvent>(
    TransactionsState()
) {

    fun onAction(action: TransactionsAction) {
        when(action) {
            TransactionsAction.SomeAction -> Unit
        }
    }

    fun getTransactions(currency: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { it.copy(isLoading = true) }
            getTransactionsUC(currency)
                .onSuccess { result ->
                    updateState {
                        it.copy(
                            isLoading = true,
                            transactions = result
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
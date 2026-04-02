package com.dmytroherez.savingstrack.presentation.savings.addtransaction

import androidx.lifecycle.viewModelScope
import com.dmytroherez.savingstrack.core.presentation.BaseViewModel
import com.dmytroherez.savingstrack.core.presentation.Extensions.toAmountMinorUnits
import com.dmytroherez.savingstrack.core.presentation.UiText
import com.dmytroherez.savingstrack.domain.usecase.savings.PostSavingUC
import com.dmytroherez.savingstrack.dto.transactions.PostTransactionRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import savingstrack.composeapp.generated.resources.Res
import savingstrack.composeapp.generated.resources.success

class AddTransactionViewModel(
    private val postSavingUC: PostSavingUC,
) : BaseViewModel<AddTransactionState, AddTransactionEvent, AddTransactionAction>(
    AddTransactionState()
) {

    override fun onAction(
        action: AddTransactionAction
    ) {
        when(action) {
            is AddTransactionAction.OnAmountChange -> updateState { it.copy(amount = action.value) }
            is AddTransactionAction.OnCategoryChange -> updateState { it.copy(category = action.value) }
            is AddTransactionAction.OnCurrencyChange -> updateState { it.copy(currency = action.value) }
            is AddTransactionAction.OnDescriptionChange -> updateState { it.copy(description = action.value) }
            is AddTransactionAction.OnSetGoal -> updateState { it.copy(goal = action.goal) }

            AddTransactionAction.OnCancelClick -> sendEvent(AddTransactionEvent.CloseBottomSheet)
            AddTransactionAction.OnSaveClick -> postSaving()
        }
    }

    private fun postSaving() {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { it.copy(isLoading = true) }

            val request = with(state.value) {
                if (amount.toAmountMinorUnits() == 0L) {
                    sendEvent(AddTransactionEvent.ShowToast(UiText.DynamicString("Invalid amount")))
                    return@launch
                }

                PostTransactionRequest(
                    currency = currency,
                    amountInMinorUnits = amount.toAmountMinorUnits(),
                    category =  category,
                    description = description,
                    goalId = goal?.id
                )
            }

            postSavingUC(request)
                .onSuccess {
                    sendEvent(
                        AddTransactionEvent.ShowToast(UiText.StringResourceId(Res.string.success))
                    )
                    sendEvent(AddTransactionEvent.CloseBottomSheet)
                    updateState { it.copy(isLoading = false) }
                }
                .onFailure { err ->
                    sendEvent(
                        AddTransactionEvent.ShowToast(UiText.DynamicString(err.message))
                    )
                    updateState { it.copy(isLoading = false) }
                }
        }
    }
}
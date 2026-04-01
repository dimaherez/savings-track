package com.dmytroherez.savingstrack.presentation.savings

import androidx.lifecycle.viewModelScope
import com.dmytroherez.savingstrack.core.presentation.BaseViewModel
import com.dmytroherez.savingstrack.core.presentation.Extensions.toAmountMinorUnits
import com.dmytroherez.savingstrack.core.presentation.UiText
import com.dmytroherez.savingstrack.domain.usecase.savings.GetSavingsDashboardUC
import com.dmytroherez.savingstrack.domain.usecase.savings.GetTransactionsByCurrencyUC
import com.dmytroherez.savingstrack.domain.usecase.savings.PostSavingUC
import com.dmytroherez.savingstrack.dto.transactions.PostTransactionRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import savingstrack.composeapp.generated.resources.Res
import savingstrack.composeapp.generated.resources.success

class SavingsViewModel(
    private val postSavingUC: PostSavingUC,
    private val getSavingsDashboardUC: GetSavingsDashboardUC
) : BaseViewModel<SavingsState, SavingsEvent>(
    SavingsState()
) {

    private val _addTransactionFormState = MutableStateFlow(AddTransactionState())
    val addTransactionFormState = _addTransactionFormState.asStateFlow()

    init {
        getSavingsDashboard()
    }

    fun onAction(action: SavingsAction) {
        when (action) {
            is SavingsAction.PostSaving -> {
                postSaving()
            }
            SavingsAction.ToggleAddSavingDialog ->
                updateState { it.copy(showAddSavingDialog = it.showAddSavingDialog.not()) }
        }
    }

    private fun getSavingsDashboard() {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { it.copy(isSavingsListLoading = true) }
            getSavingsDashboardUC()
                .onSuccess { response ->
                    updateState {
                        it.copy(
                            savings = response,
                            isSavingsListLoading = false
                        )
                    }
                }
                .onFailure { err ->
                    sendEvent(SavingsEvent.ShowToast(UiText.DynamicString(err.message)))
                    updateState { it.copy(isSavingsListLoading = false) }
                }
        }
    }

//    private fun getSavings() {
//        viewModelScope.launch(Dispatchers.IO) {
//            updateState { it.copy(isSavingsListLoading = true) }
//            getSavingsUC()
//                .onSuccess { resultList ->
//                    updateState {
//                        it.copy(
//                            savings = resultList,
//                            isSavingsListLoading = false
//                        )
//                    }
//                }
//                .onFailure { err ->
//                    sendEvent(SavingsEvent.ShowToast(UiText.DynamicString(err.message)))
//                    updateState { it.copy(isSavingsListLoading = false) }
//                }
//        }
//    }

    private fun postSaving() {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { it.copy(isAddSavingLoading = true) }

            val request = with(addTransactionFormState.value) {
                if (amount.toAmountMinorUnits() == 0L) {
                    sendEvent(SavingsEvent.ShowToast(UiText.DynamicString("Invalid amount")))
                    return@launch
                }

                PostTransactionRequest(
                    currency = currency,
                    amountInMinorUnits = amount.toAmountMinorUnits(),
                    category =  category,
                    description = description,
                    goalId = goalId
                )
            }

            postSavingUC(request)
                .onSuccess {
                    sendEvent(
                        SavingsEvent.ShowToast(UiText.StringResourceId(Res.string.success))
                    )
                    getSavingsDashboard()
                    onAddSavingComplete()
                }
                .onFailure { err ->
                    sendEvent(
                        SavingsEvent.ShowToast(UiText.DynamicString(err.message))
                    )
                    onAddSavingComplete()
                }
        }
    }

    private fun onAddSavingComplete() {
        updateState {
            it.copy(
                showAddSavingDialog = false,
                isAddSavingLoading = false
            )
        }
    }
}

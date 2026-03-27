package com.dmytroherez.savingstrack.presentation.savings

import androidx.lifecycle.viewModelScope
import com.dmytroherez.savingstrack.core.presentation.BaseViewModel
import com.dmytroherez.savingstrack.core.presentation.UiText
import com.dmytroherez.savingstrack.domain.usecase.savings.GetSavingsUC
import com.dmytroherez.savingstrack.domain.usecase.savings.PostSavingUC
import com.dmytroherez.savingstrack.dto.savings.PostTransactionRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import savingstrack.composeapp.generated.resources.Res
import savingstrack.composeapp.generated.resources.success

class SavingsViewModel(
    private val getSavingsUC: GetSavingsUC,
    private val postSavingUC: PostSavingUC
) : BaseViewModel<SavingsState, SavingsEvent>(
    SavingsState()
) {

    init {
        getSavings()
    }

    fun onAction(action: SavingsAction) {
        when (action) {
            is SavingsAction.PostSaving -> postSaving(action.saving)
            SavingsAction.ToggleAddSavingDialog ->
                updateState { it.copy(showAddSavingDialog = it.showAddSavingDialog.not()) }
        }
    }

    private fun getSavings() {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { it.copy(isSavingsListLoading = true) }
            getSavingsUC()
                .onSuccess { resultList ->
                    updateState {
                        it.copy(
                            savings = resultList,
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

    private fun postSaving(request: PostTransactionRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { it.copy(isAddSavingLoading = true) }
            postSavingUC(request)
                .onSuccess {
                    sendEvent(
                        SavingsEvent.ShowToast(UiText.StringResourceId(Res.string.success))
                    )
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

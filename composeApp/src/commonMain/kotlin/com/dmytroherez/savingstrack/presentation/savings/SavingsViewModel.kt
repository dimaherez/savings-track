package com.dmytroherez.savingstrack.presentation.savings

import androidx.lifecycle.viewModelScope
import com.dmytroherez.savingstrack.core.presentation.BaseViewModel
import com.dmytroherez.savingstrack.core.presentation.UiText
import com.dmytroherez.savingstrack.domain.usecase.savings.GetSavingsDashboardUC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class SavingsViewModel(
    private val getSavingsDashboardUC: GetSavingsDashboardUC
) : BaseViewModel<SavingsState, SavingsEvent>(
    SavingsState()
) {
    init {
        getSavingsDashboard()
    }

    fun onAction(action: SavingsAction) {
        when (action) {
            else -> {}
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
}

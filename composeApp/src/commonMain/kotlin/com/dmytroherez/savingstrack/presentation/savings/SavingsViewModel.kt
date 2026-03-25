package com.dmytroherez.savingstrack.presentation.savings

import androidx.lifecycle.viewModelScope
import com.dmytroherez.savingstrack.core.presentation.BaseViewModel
import com.dmytroherez.savingstrack.core.presentation.UiText
import com.dmytroherez.savingstrack.domain.usecase.savings.GetSavingsUC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class SavingsViewModel(
    private val getSavingsUC: GetSavingsUC
) : BaseViewModel<FiatState, SavingsEvent>(
    FiatState()
) {

    init {
        getSavings()
    }

    private fun getSavings() {
        viewModelScope.launch(Dispatchers.IO) {
            getSavingsUC()
                .onSuccess { resultList ->
                    updateState {
                        it.copy(savings = resultList)
                    }
                }
                .onFailure {
                    sendEvent(SavingsEvent.ShowErrorToast(UiText.DynamicString(it.message)))
                }
        }
    }

    fun onAction(action: SavingsAction) {
        when (action) {
            SavingsAction.SomeAction -> {

            }
        }
    }
}

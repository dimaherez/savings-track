package com.dmytroherez.savingstrack.presentation.home.addgoal

import androidx.lifecycle.viewModelScope
import com.dmytroherez.savingstrack.core.presentation.BaseViewModel
import com.dmytroherez.savingstrack.core.presentation.Extensions.toAmountMinorUnits
import com.dmytroherez.savingstrack.core.presentation.UiText
import com.dmytroherez.savingstrack.domain.usecase.goals.AddGoalUC
import com.dmytroherez.savingstrack.dto.goals.CreateGoalRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

class AddGoalViewModel(
    private val addGoalUC: AddGoalUC
) : BaseViewModel<AddGoalState, AddGoalEvent>(
    AddGoalState()
) {


    fun onAction(action: AddGoalAction) {
        when(action) {
            is AddGoalAction.OnTitleChange -> updateState { it.copy(title = action.value) }
            is AddGoalAction.OnAmountChange -> updateState { it.copy(amount = action.value) }
            is AddGoalAction.OnCurrencyChange -> updateState { it.copy(currency = action.value) }
            is AddGoalAction.OnDateChange -> {
                updateState {
                    it.copy(
                        selectedDate = action.value?.let { dateLong ->
                            Instant.fromEpochMilliseconds(dateLong)
                                .toLocalDateTime(TimeZone.currentSystemDefault()).date
                        },
                        showDatePicker = false
                    )
                }
            }
            AddGoalAction.ToggleDatePicker -> updateState { it.copy(showDatePicker = it.showDatePicker.not()) }

            AddGoalAction.AddGoal -> addGoal()
            AddGoalAction.Dismiss -> dismiss()
        }
    }

    private fun dismiss() = sendEvent(AddGoalEvent.Dismiss)

    private fun addGoal() {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { it.copy(isLoading = true) }

            val request = with(state.value) {
                CreateGoalRequest(
                    title = title,
                    targetAmountInMinorUnits = amount.toAmountMinorUnits(),
                    currency = currency,
                    deadline = selectedDate
                )
            }

            addGoalUC(request)
                .onSuccess {
                    updateState {
                        it.copy(
                            isLoading = false
                        )
                    }
                    dismiss()
                }
                .onFailure { err ->
                    sendEvent(AddGoalEvent.ShowToast(UiText.DynamicString(err.message)))
                    updateState { it.copy(isLoading = false) }
                }
        }
    }


}
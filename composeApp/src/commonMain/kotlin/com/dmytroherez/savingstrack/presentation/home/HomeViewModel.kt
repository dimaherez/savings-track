package com.dmytroherez.savingstrack.presentation.home

import androidx.lifecycle.viewModelScope
import com.dmytroherez.savingstrack.core.presentation.BaseViewModel
import com.dmytroherez.savingstrack.core.presentation.UiText
import com.dmytroherez.savingstrack.domain.usecase.goals.AddGoalUC
import com.dmytroherez.savingstrack.domain.usecase.goals.GetGoalsUC
import com.dmytroherez.savingstrack.dto.goals.CreateGoalRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class HomeViewModel(
    private val addGoalUC: AddGoalUC,
    private val getGoalsUC: GetGoalsUC
) : BaseViewModel<HomeState, HomeEvent>(
    HomeState()
) {

    init {
        getGoals()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.ToggleAddGoalDialog -> {
                updateState { it.copy(showAddGoalDialog = it.showAddGoalDialog.not()) }
            }

            is HomeAction.AddGoal -> {
                addGoal(action.request)
            }
        }
    }

    private fun getGoals() {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { it.copy(goalsLoading = true) }
            getGoalsUC()
                .onSuccess { result ->
                    updateState {
                        it.copy(
                            goalsLoading = false,
                            goals = result
                        )
                    }

                }
                .onFailure { err ->
                    sendEvent(HomeEvent.ShowToast(UiText.DynamicString(err.message)))
                    updateState { it.copy(goalsLoading = false) }
                }
        }
    }

    private fun addGoal(
        request: CreateGoalRequest
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { it.copy(isGoalAddingLoading = true) }
            addGoalUC(request)
                .onSuccess {
                    updateState {
                        it.copy(
                            isGoalAddingLoading = false,
                            showAddGoalDialog = false
                        )
                    }
                    getGoals()
                }
                .onFailure { err ->
                    sendEvent(HomeEvent.ShowToast(UiText.DynamicString(err.message)))
                    updateState { it.copy(isGoalAddingLoading = false) }
                }
        }
    }
}
package com.dmytroherez.savingstrack.presentation.home

import androidx.lifecycle.viewModelScope
import com.dmytroherez.savingstrack.core.presentation.BaseViewModel
import com.dmytroherez.savingstrack.core.presentation.UiText
import com.dmytroherez.savingstrack.domain.usecase.goals.CompleteGoalUC
import com.dmytroherez.savingstrack.domain.usecase.goals.GetGoalsUC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getGoalsUC: GetGoalsUC,
    private val completeGoalUC: CompleteGoalUC
) : BaseViewModel<HomeState, HomeEvent>(
    HomeState()
) {

    init {
        getGoals()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.CompleteGoal -> completeGoal(action.goalId)
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


    private fun completeGoal(goalId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            completeGoalUC(goalId)
                .onSuccess {
                    getGoals()
                }
                .onFailure { err ->
                    sendEvent(HomeEvent.ShowToast(UiText.DynamicString(err.message)))
                }
        }
    }
}
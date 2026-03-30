package com.dmytroherez.savingstrack.presentation.home

import com.dmytroherez.savingstrack.core.presentation.BaseUiState
import com.dmytroherez.savingstrack.dto.goals.GoalItem

data class HomeState(
    val goals: List<GoalItem> = emptyList(),
    val goalsLoading: Boolean = false,
    val showAddGoalDialog: Boolean = false,
    val isGoalAddingLoading: Boolean = false
) : BaseUiState

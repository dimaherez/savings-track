package com.dmytroherez.savingstrack.domain.repo

import com.dmytroherez.savingstrack.shared.dto.goals.CreateGoalRequest
import com.dmytroherez.savingstrack.shared.dto.goals.GoalForTransactionItem
import com.dmytroherez.savingstrack.shared.dto.goals.GoalItem
import kotlinx.coroutines.flow.SharedFlow

interface GoalsRepo {
    suspend fun addGoal(request: CreateGoalRequest): Result<Unit>
    suspend fun getGoals() : Result<List<GoalItem>>
    suspend fun getAvailableGoals() : Result<List<GoalForTransactionItem>>
    suspend fun completeGoal(goalId: Int) : Result<Unit>

    val refreshTrigger: SharedFlow<Unit>
}
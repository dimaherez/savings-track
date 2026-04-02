package com.dmytroherez.savingstrack.domain.repo

import com.dmytroherez.savingstrack.dto.goals.CreateGoalRequest
import com.dmytroherez.savingstrack.dto.goals.GoalForTransactionItem
import com.dmytroherez.savingstrack.dto.goals.GoalItem

interface GoalsRepo {
    suspend fun addGoal(request: CreateGoalRequest): Result<Unit>
    suspend fun getGoals() : Result<List<GoalItem>>
    suspend fun getAvailableGoals() : Result<List<GoalForTransactionItem>>
    suspend fun completeGoal(goalId: Int) : Result<Unit>
}
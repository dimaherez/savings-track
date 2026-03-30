package com.dmytroherez.savingstrack.domain.repo

import com.dmytroherez.savingstrack.dto.goals.CreateGoalRequest
import com.dmytroherez.savingstrack.dto.goals.GoalItem

interface GoalsRepo {
    suspend fun addGoal(request: CreateGoalRequest): Result<Unit>
    suspend fun getGoals() : Result<List<GoalItem>>
}
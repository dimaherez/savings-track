package com.dmytroherez.savingstrack.domain.repo

import com.dmytroherez.savingstrack.dto.goals.CreateGoalRequest
import com.dmytroherez.savingstrack.dto.goals.GoalForTransactionItem
import com.dmytroherez.savingstrack.dto.goals.GoalItem

interface GoalsRepo {
    suspend fun addGoal(userId: String, request: CreateGoalRequest)
    suspend fun getGoals(userId: String) : List<GoalItem>
    suspend fun setAsCompleted(goalId: Int)

    suspend fun getGoalsForTransaction(userId: String): List<GoalForTransactionItem>
}
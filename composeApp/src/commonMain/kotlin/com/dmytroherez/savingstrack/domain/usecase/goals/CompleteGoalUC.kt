package com.dmytroherez.savingstrack.domain.usecase.goals

import com.dmytroherez.savingstrack.domain.repo.GoalsRepo

class CompleteGoalUC (private val goalsRepo: GoalsRepo) {
    suspend operator fun invoke(goalId: Int) = goalsRepo.completeGoal(goalId)
}
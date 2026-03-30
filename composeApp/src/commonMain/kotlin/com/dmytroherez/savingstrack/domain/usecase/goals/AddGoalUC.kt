package com.dmytroherez.savingstrack.domain.usecase.goals

import com.dmytroherez.savingstrack.domain.repo.GoalsRepo
import com.dmytroherez.savingstrack.dto.goals.CreateGoalRequest

class AddGoalUC (private val goalsRepo: GoalsRepo) {
    suspend operator fun invoke(request: CreateGoalRequest) = goalsRepo.addGoal(request)
}
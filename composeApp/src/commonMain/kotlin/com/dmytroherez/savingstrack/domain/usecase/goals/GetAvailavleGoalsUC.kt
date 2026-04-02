package com.dmytroherez.savingstrack.domain.usecase.goals

import com.dmytroherez.savingstrack.domain.repo.GoalsRepo

class GetAvailableGoalsUC (private val goalsRepo: GoalsRepo) {
    suspend operator fun invoke() = goalsRepo.getAvailableGoals()
}
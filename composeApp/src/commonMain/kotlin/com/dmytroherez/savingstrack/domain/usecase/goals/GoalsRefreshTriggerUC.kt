package com.dmytroherez.savingstrack.domain.usecase.goals

import com.dmytroherez.savingstrack.domain.repo.GoalsRepo

class GoalsRefreshTriggerUC (private val goalsRepo: GoalsRepo) {
    operator fun invoke() = goalsRepo.refreshTrigger
}
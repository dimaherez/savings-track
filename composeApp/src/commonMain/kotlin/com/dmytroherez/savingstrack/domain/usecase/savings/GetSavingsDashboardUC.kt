package com.dmytroherez.savingstrack.domain.usecase.savings

import com.dmytroherez.savingstrack.domain.repo.SavingsRepo

class GetSavingsDashboardUC (private val savingsRepo: SavingsRepo) {
    suspend operator fun invoke() = savingsRepo.getSavingsDashboard()
}
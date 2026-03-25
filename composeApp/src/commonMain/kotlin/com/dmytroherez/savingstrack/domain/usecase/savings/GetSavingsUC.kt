package com.dmytroherez.savingstrack.domain.usecase.savings

import com.dmytroherez.savingstrack.domain.repo.SavingsRepo

class GetSavingsUC (private val savingsRepo: SavingsRepo) {
    suspend operator fun invoke() = savingsRepo.getSavings()
}
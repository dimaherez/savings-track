package com.dmytroherez.savingstrack.domain.usecase.savings

import com.dmytroherez.savingstrack.domain.repo.SavingsRepo

class TransactionsRefreshTriggerUC (private val savingsRepo: SavingsRepo) {
    operator fun invoke() = savingsRepo.refreshTrigger
}
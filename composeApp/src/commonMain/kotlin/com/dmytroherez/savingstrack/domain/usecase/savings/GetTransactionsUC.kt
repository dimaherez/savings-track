package com.dmytroherez.savingstrack.domain.usecase.savings

import com.dmytroherez.savingstrack.domain.repo.SavingsRepo

class GetTransactionsUC (private val savingsRepo: SavingsRepo) {
    suspend operator fun invoke(currency: String) = savingsRepo.getSavings(currency)
}
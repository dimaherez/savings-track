package com.dmytroherez.savingstrack.domain.usecase.savings

import com.dmytroherez.savingstrack.domain.repo.SavingsRepo

class GetTransactionsByCurrencyUC (private val savingsRepo: SavingsRepo) {
    suspend operator fun invoke(currency: String) = savingsRepo.getTransactionsByCurrency(currency)
}
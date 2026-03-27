package com.dmytroherez.savingstrack.domain.usecase.savings

import com.dmytroherez.savingstrack.domain.repo.SavingsRepo
import com.dmytroherez.savingstrack.dto.transactions.PostTransactionRequest

class PostSavingUC (private val savingsRepo: SavingsRepo) {
    suspend operator fun invoke(
        request: PostTransactionRequest
    ) = savingsRepo.postSaving(
        request = request
    )
}
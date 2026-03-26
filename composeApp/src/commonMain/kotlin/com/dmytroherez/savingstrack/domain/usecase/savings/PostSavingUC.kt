package com.dmytroherez.savingstrack.domain.usecase.savings

import com.dmytroherez.savingstrack.domain.repo.SavingsRepo
import com.dmytroherez.savingstrack.dto.savings.PostSavingRequest

class PostSavingUC (private val savingsRepo: SavingsRepo) {
    suspend operator fun invoke(
        request: PostSavingRequest
    ) = savingsRepo.postSaving(
        request = request
    )
}
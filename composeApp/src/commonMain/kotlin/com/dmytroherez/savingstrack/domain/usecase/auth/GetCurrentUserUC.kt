package com.dmytroherez.savingstrack.domain.usecase.auth

import com.dmytroherez.savingstrack.domain.repo.AuthRepo

class GetCurrentUserUC (private val authRepo: AuthRepo) {
    operator fun invoke() = authRepo.getCurrentUser()
}
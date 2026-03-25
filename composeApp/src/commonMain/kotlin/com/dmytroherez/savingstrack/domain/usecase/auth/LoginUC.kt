package com.dmytroherez.savingstrack.domain.usecase.auth

import com.dmytroherez.savingstrack.domain.repo.AuthRepo

class LoginUC(private val authRepo: AuthRepo) {
    suspend operator fun invoke(
        email: String,
        pass: String
    ) = authRepo.login(email, pass)
}
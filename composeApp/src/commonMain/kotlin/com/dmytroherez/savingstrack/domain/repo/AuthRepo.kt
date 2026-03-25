package com.dmytroherez.savingstrack.domain.repo

interface AuthRepo {
    suspend fun register(email: String, pass: String): Result<String>
    suspend fun login(email: String, pass: String): Result<String>
    suspend fun logout()
    fun getCurrentUser(): Result<Unit>
}
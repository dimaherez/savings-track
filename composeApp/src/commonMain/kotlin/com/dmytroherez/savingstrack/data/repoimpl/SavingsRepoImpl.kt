package com.dmytroherez.savingstrack.data.repoimpl

import com.dmytroherez.savingstrack.core.network.httpClient
import com.dmytroherez.savingstrack.domain.repo.SavingsRepo
import com.dmytroherez.savingstrack.dto.savings.GetSavingsResponseItem
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import io.ktor.client.call.body
import io.ktor.client.request.get

class SavingsRepoImpl : SavingsRepo {
    private val baseUrl = "http://10.10.10.110:8080"

    override suspend fun postSaving(
        userId: String,
        currency: String,
        amount: Double,
        description: String?
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getSavings(): Result<List<GetSavingsResponseItem>> {
        return try {
            val response = httpClient.get("$baseUrl/savings/${Firebase.auth.currentUser!!.uid}")
            Result.success(response.body())
        } catch (e: Exception) {
            println("Network error: ${e.message}")
            Result.failure(e)
        }
    }

}
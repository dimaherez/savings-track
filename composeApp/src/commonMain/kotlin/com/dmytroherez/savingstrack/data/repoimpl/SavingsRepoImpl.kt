package com.dmytroherez.savingstrack.data.repoimpl

import co.touchlab.kermit.Logger
import com.dmytroherez.savingstrack.domain.repo.SavingsRepo
import com.dmytroherez.savingstrack.dto.savings.PostSavingRequest
import com.dmytroherez.savingstrack.dto.savings.SavingItem
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class SavingsRepoImpl(
    private val httpClient: HttpClient
) : SavingsRepo {
    override suspend fun postSaving(
        request: PostSavingRequest
    ): Result<Unit> {
        return try {
            httpClient.post("savings/add") {
                setBody(request)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Logger.e(e) { "SavingsRepoImpl.postSaving()" }
            Result.failure(e)
        }
    }

    override suspend fun getSavings(): Result<List<SavingItem>> {
        return try {
            val response = httpClient.get("savings/list")
            Result.success(response.body())
        } catch (e: Exception) {
            Logger.e(e) { "SavingsRepoImpl.getSavings()" }
            Result.failure(e)
        }
    }

}
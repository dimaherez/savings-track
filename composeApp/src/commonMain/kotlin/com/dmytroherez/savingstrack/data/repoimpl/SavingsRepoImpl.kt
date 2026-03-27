package com.dmytroherez.savingstrack.data.repoimpl

import co.touchlab.kermit.Logger
import com.dmytroherez.savingstrack.domain.repo.SavingsRepo
import com.dmytroherez.savingstrack.dto.transactions.DashboardResponse
import com.dmytroherez.savingstrack.dto.transactions.PostTransactionRequest
import com.dmytroherez.savingstrack.dto.transactions.TransactionItem
import com.dmytroherez.savingstrack.routes.RoutePath.PATH_TRANSACTIONS_DASHBOARD
import com.dmytroherez.savingstrack.routes.RoutePath.PATH_TRANSACTIONS_LIST_TRANSACTIONS
import com.dmytroherez.savingstrack.routes.RoutePath.PATH_TRANSACTIONS_POST_TRANSACTION
import com.dmytroherez.savingstrack.routes.RoutePath.PATH_TRANSACTIONS_PREFIX
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class SavingsRepoImpl(
    private val httpClient: HttpClient
) : SavingsRepo {
    override suspend fun postSaving(
        request: PostTransactionRequest
    ): Result<Unit> {
        return try {
            httpClient.post("$PATH_TRANSACTIONS_PREFIX$PATH_TRANSACTIONS_POST_TRANSACTION") {
                setBody(request)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Logger.e(e) { "SavingsRepoImpl.postSaving()" }
            Result.failure(e)
        }
    }

    override suspend fun getSavings(currency: String): Result<List<TransactionItem>> {
        return try {
            val response = httpClient.get("$PATH_TRANSACTIONS_PREFIX$PATH_TRANSACTIONS_LIST_TRANSACTIONS/$currency")
            Result.success(response.body())
        } catch (e: Exception) {
            Logger.e(e) { "SavingsRepoImpl.getSavings()" }
            Result.failure(e)
        }
    }

    override suspend fun getSavingsDashboard(): Result<DashboardResponse> {
        return try {
            val response = httpClient.get("$PATH_TRANSACTIONS_PREFIX$PATH_TRANSACTIONS_DASHBOARD")
            Result.success(response.body())
        } catch (e: Exception) {
            Logger.e(e) { "SavingsRepoImpl.getSavingsDashboard()" }
            Result.failure(e)
        }
    }

}
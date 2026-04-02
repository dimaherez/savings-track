package com.dmytroherez.savingstrack.data.repoimpl

import co.touchlab.kermit.Logger
import com.dmytroherez.savingstrack.domain.repo.SavingsRepo
import com.dmytroherez.savingstrack.shared.dto.transactions.DashboardResponse
import com.dmytroherez.savingstrack.shared.dto.transactions.PostTransactionRequest
import com.dmytroherez.savingstrack.shared.dto.transactions.TransactionsByCurrencyResponse
import com.dmytroherez.savingstrack.shared.routes.TransactionsRoute
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody

class SavingsRepoImpl(
    private val httpClient: HttpClient
) : SavingsRepo {
    override suspend fun postSaving(
        request: PostTransactionRequest
    ): Result<Unit> {
        return try {
            httpClient.post(TransactionsRoute.Add()) {
                setBody(request)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Logger.e(e) { "SavingsRepoImpl.postSaving()" }
            Result.failure(e)
        }
    }

    override suspend fun getTransactionsByCurrency(currency: String): Result<TransactionsByCurrencyResponse> {
        return try {
            val response =
                httpClient.get(TransactionsRoute.GetByCurrency(currency = currency))
            Result.success(response.body())
        } catch (e: Exception) {
            Logger.e(e) { "SavingsRepoImpl.getSavings()" }
            Result.failure(e)
        }
    }

    override suspend fun getSavingsDashboard(): Result<DashboardResponse> {
        return try {
            val response = httpClient.get(TransactionsRoute.Dashboard())
            Result.success(response.body())
        } catch (e: Exception) {
            Logger.e(e) { "SavingsRepoImpl.getSavingsDashboard()" }
            Result.failure(e)
        }
    }

}
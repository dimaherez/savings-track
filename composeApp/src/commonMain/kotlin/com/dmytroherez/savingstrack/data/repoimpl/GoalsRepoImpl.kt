package com.dmytroherez.savingstrack.data.repoimpl

import co.touchlab.kermit.Logger
import com.dmytroherez.savingstrack.domain.repo.GoalsRepo
import com.dmytroherez.savingstrack.shared.dto.goals.CreateGoalRequest
import com.dmytroherez.savingstrack.shared.dto.goals.GoalForTransactionItem
import com.dmytroherez.savingstrack.shared.dto.goals.GoalItem
import com.dmytroherez.savingstrack.shared.routes.GoalsRoute
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.patch
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody

class GoalsRepoImpl(
    private val httpClient: HttpClient
) : GoalsRepo {
    override suspend fun addGoal(
        request: CreateGoalRequest
    ): Result<Unit> {
        return try {
            httpClient.post(GoalsRoute.Add()) {
                setBody(request)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Logger.e(e) { "GoalsRepoImpl.addGoal()" }
            Result.failure(e)
        }
    }

    override suspend fun getGoals(): Result<List<GoalItem>> {
        return try {
            val response = httpClient.get(GoalsRoute.ListAll())
            Result.success(response.body())
        } catch (e: Exception) {
            Logger.e(e) { "GoalsRepoImpl.getGoals()" }
            Result.failure(e)
        }
    }

    override suspend fun getAvailableGoals(): Result<List<GoalForTransactionItem>> {
        return try {
            val response = httpClient.get(GoalsRoute.Available())
            Result.success(response.body())
        } catch (e: Exception) {
            Logger.e(e) { "GoalsRepoImpl.getAvailableGoals()" }
            Result.failure(e)
        }
    }

    override suspend fun completeGoal(goalId: Int): Result<Unit> {
        return try {
            httpClient.patch(GoalsRoute.Complete(goalId = goalId))
            Result.success(Unit)
        } catch (e: Exception) {
            Logger.e(e) { "GoalsRepoImpl.completeGoal()" }
            Result.failure(e)
        }
    }
}
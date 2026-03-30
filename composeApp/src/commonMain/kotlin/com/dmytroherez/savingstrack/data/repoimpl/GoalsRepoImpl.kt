package com.dmytroherez.savingstrack.data.repoimpl

import co.touchlab.kermit.Logger
import com.dmytroherez.savingstrack.domain.repo.GoalsRepo
import com.dmytroherez.savingstrack.dto.goals.CreateGoalRequest
import com.dmytroherez.savingstrack.dto.goals.GoalItem
import com.dmytroherez.savingstrack.routes.RoutePath.PATH_GOALS_PREFIX
import com.dmytroherez.savingstrack.routes.RoutePath.PATH_SUFFIX_ADD
import com.dmytroherez.savingstrack.routes.RoutePath.PATH_SUFFIX_LIST
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class GoalsRepoImpl(
    private val httpClient: HttpClient
) : GoalsRepo {
    override suspend fun addGoal(
        request: CreateGoalRequest
    ): Result<Unit> {
        return try {
            httpClient.post("$PATH_GOALS_PREFIX$PATH_SUFFIX_ADD"){
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
            val response = httpClient.get("$PATH_GOALS_PREFIX$PATH_SUFFIX_LIST")
            Result.success(response.body())
        } catch (e: Exception) {
            Logger.e(e) { "GoalsRepoImpl.getGoals()" }
            Result.failure(e)
        }
    }
}
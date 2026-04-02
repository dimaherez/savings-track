package com.dmytroherez.savingstrack.routing

import com.dmytroherez.savingstrack.Constants.FIELD_GOAL_ID
import com.dmytroherez.savingstrack.Constants.JWT_NAME
import com.dmytroherez.savingstrack.domain.repo.GoalsRepo
import com.dmytroherez.savingstrack.dto.goals.CreateGoalRequest
import com.dmytroherez.savingstrack.routes.RoutePath.PATH_GOALS_AVAILABLE
import com.dmytroherez.savingstrack.routes.RoutePath.PATH_GOALS_PREFIX
import com.dmytroherez.savingstrack.routes.RoutePath.PATH_GOALS_SET_COMPLETED
import com.dmytroherez.savingstrack.routes.RoutePath.PATH_SUFFIX_ADD
import com.dmytroherez.savingstrack.routes.RoutePath.PATH_SUFFIX_LIST
import com.dmytroherez.savingstrack.withSecureUid
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Routing.goalsRoutes() {
    val repository by inject<GoalsRepo>()

    authenticate(JWT_NAME) {
        route(PATH_GOALS_PREFIX) {
            get(PATH_SUFFIX_LIST) {
                withSecureUid { uid ->
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = repository.getGoals(uid)
                    )
                }
            }

            get("$PATH_SUFFIX_LIST/$PATH_GOALS_AVAILABLE") {
                withSecureUid { uid ->
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = repository.getGoalsForTransaction(uid)
                    )
                }
            }

            post(PATH_SUFFIX_ADD) {
                withSecureUid { uid ->
                    repository.addGoal(
                        userId = uid,
                        request = call.receive<CreateGoalRequest>()
                    )
                    call.respond(HttpStatusCode.Created)
                }
            }

            patch("$PATH_GOALS_SET_COMPLETED/{$FIELD_GOAL_ID}") {
                withSecureUid {
                    call.pathParameters[FIELD_GOAL_ID]?.toIntOrNull()?.let { goalId ->
                        repository.setAsCompleted(goalId)
                        call.respond(HttpStatusCode.OK)
                    } ?: call.respond(HttpStatusCode.BadRequest, "Goal id in path parameters is invalid or missing")
                }
            }
        }
    }
}
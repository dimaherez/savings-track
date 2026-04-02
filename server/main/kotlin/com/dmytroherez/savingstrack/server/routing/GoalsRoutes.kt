package com.dmytroherez.savingstrack.server.routing

import com.dmytroherez.savingstrack.server.Constants.JWT_NAME
import com.dmytroherez.savingstrack.server.domain.repo.GoalsRepo
import com.dmytroherez.savingstrack.shared.dto.goals.CreateGoalRequest
import com.dmytroherez.savingstrack.shared.routes.GoalsRoute
import com.dmytroherez.savingstrack.server.withSecureUid
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject

fun Routing.goalsRoutes() {
    val repository by inject<GoalsRepo>()

    authenticate(JWT_NAME) {
        get<GoalsRoute.ListAll> {
            withSecureUid { uid ->
                call.respond(
                    status = HttpStatusCode.OK,
                    message = repository.getGoals(uid)
                )
            }
        }

        get<GoalsRoute.Available> {
            withSecureUid { uid ->
                call.respond(
                    status = HttpStatusCode.OK,
                    message = repository.getGoalsForTransaction(uid)
                )
            }
        }

        post<GoalsRoute.Add> {
            withSecureUid { uid ->
                repository.addGoal(
                    userId = uid,
                    request = call.receive<CreateGoalRequest>()
                )
                call.respond(HttpStatusCode.Created)
            }
        }

        patch<GoalsRoute.Complete> { resource ->
            withSecureUid {
                repository.setAsCompleted(resource.goalId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
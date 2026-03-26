package com.dmytroherez.savingstrack.routing

import com.dmytroherez.savingstrack.Constants.JWT_NAME
import com.dmytroherez.savingstrack.data.repo.SavingsRepository
import com.dmytroherez.savingstrack.dto.savings.PostSavingRequest
import com.dmytroherez.savingstrack.withSecureUid
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Routing.postSaving(repository: SavingsRepository) {
    authenticate(JWT_NAME) {
        post("/savings/add") {
            withSecureUid { uid ->
                repository.addSaving(uid, call.receive<PostSavingRequest>())
                call.respond(HttpStatusCode.Created)
            }
        }
    }
}

fun Routing.getAllSavings(repository: SavingsRepository) {
    authenticate(JWT_NAME) {
        get("/savings/list") {
            withSecureUid { uid ->
                call.respond(
                    status = HttpStatusCode.OK,
                    message = repository.getAllSavings(uid)
                )
            }
        }
    }
}

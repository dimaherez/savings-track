package com.dmytroherez.savingstrack.routing

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
    authenticate("firebase-auth") {
        post("/savings") {
            withSecureUid { uid ->
                val newSaving = call.receive<PostSavingRequest>()
                val generatedId = repository.addSaving(uid, newSaving)
                call.respond(HttpStatusCode.Created, mapOf("id" to generatedId))
            }
        }
    }
}

fun Routing.getAllSavings(repository: SavingsRepository) {
    authenticate("firebase-auth") {
        get("/savings/list") {
            withSecureUid { uid ->
                val savingsList = repository.getAllSavings(uid)
                call.respond(HttpStatusCode.OK, savingsList)
            }
        }
    }
}

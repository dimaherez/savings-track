package com.savingstrack.routing

import com.savingstrack.repo.SavingsRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import org.example.dto.savings.PostSavingRequest

fun Routing.postSaving(repository: SavingsRepository) {
    post("/savings") {
        val newSaving = call.receive<PostSavingRequest>()
        val generatedId = repository.addSaving(newSaving)
        call.respond(HttpStatusCode.Created, mapOf("id" to generatedId))
    }
}

fun Routing.getAllSavings(repository: SavingsRepository) {
    get("/savings/{userId}") {
        try {
            val savingsList = repository.getAllSavings(call.parameters["userId"]!!)
            call.respond(HttpStatusCode.OK, savingsList)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "User id is not provided")
        }
    }
}
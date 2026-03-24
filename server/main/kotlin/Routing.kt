package com.savingstrack

import com.savingstrack.repo.SavingsRepository
import com.savingstrack.routing.postSaving
import com.savingstrack.routing.getAllSavings
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        val repository = SavingsRepository()
        postSaving(repository)
        getAllSavings(repository)
    }
}

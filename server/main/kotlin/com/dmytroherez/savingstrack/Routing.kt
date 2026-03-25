package com.dmytroherez.savingstrack

import com.dmytroherez.savingstrack.data.repo.SavingsRepository
import com.dmytroherez.savingstrack.routing.postSaving
import com.dmytroherez.savingstrack.routing.getAllSavings
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

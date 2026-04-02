package com.dmytroherez.savingstrack.server

import com.dmytroherez.savingstrack.server.routing.goalsRoutes
import com.dmytroherez.savingstrack.server.routing.transactionsRoutes
import io.ktor.server.application.Application
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        transactionsRoutes()
        goalsRoutes()
    }
}

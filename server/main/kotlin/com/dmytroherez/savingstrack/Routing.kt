package com.dmytroherez.savingstrack

import com.dmytroherez.savingstrack.routing.transactionsRoutes
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
    }
}

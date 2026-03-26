package com.dmytroherez.savingstrack

import com.auth0.jwk.JwkProviderBuilder
import java.net.URI
import com.dmytroherez.savingstrack.data.tables.SavingsTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.path
import io.ktor.server.response.respond
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.slf4j.event.Level
import java.util.concurrent.TimeUnit

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(ContentNegotiation) {
        json()
    }

    installAuth()

    initDatabase()

    configureRouting()
}

private fun Application.installAuth() {
    val firebaseProjectId = "savingstrack-669e8"

    val jwkProvider =
        JwkProviderBuilder(URI("https://www.googleapis.com/service_accounts/v1/jwk/securetoken@system.gserviceaccount.com").toURL())
            .cached(10, 24, TimeUnit.HOURS)
            .build()

    install(Authentication) {
        jwt("firebase-auth") {
            verifier(jwkProvider, "https://securetoken.google.com/$firebaseProjectId") {
                acceptLeeway(3)
            }
            validate { credential ->
                if (credential.payload.audience.contains(firebaseProjectId)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(io.ktor.http.HttpStatusCode.Unauthorized, "Invalid or missing token")
            }
        }
    }
}

private fun initDatabase() {
    val config = HikariConfig().apply {
        jdbcUrl = System.getenv("DB_URL")
        username = System.getenv("DB_USER")
        password = System.getenv("DB_PASSWORD")
        driverClassName = "org.postgresql.Driver"
        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }

    Database.connect(HikariDataSource(config))

    transaction {
        SchemaUtils.create(SavingsTable)
    }
}

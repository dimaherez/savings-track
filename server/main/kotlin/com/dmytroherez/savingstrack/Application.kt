package com.dmytroherez.savingstrack

import com.auth0.jwk.JwkProviderBuilder
import com.dmytroherez.savingstrack.Constants.FIREBASE_PROJECT_ID
import com.dmytroherez.savingstrack.Constants.JWK_PROVIDER_URL
import com.dmytroherez.savingstrack.Constants.JWT_NAME
import com.dmytroherez.savingstrack.Constants.JWT_PROVIDER_ISSUER_URL
import com.dmytroherez.savingstrack.data.tables.TransactionsTable
import com.dmytroherez.savingstrack.di.appModule
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
import org.koin.ktor.plugin.Koin
import org.slf4j.event.Level
import java.net.URI
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

    install(Koin) {
        modules(appModule)
    }

    installAuth()

    initDatabase()

    configureRouting()
}

private fun Application.installAuth() {
    val jwkProvider =
        JwkProviderBuilder(URI(JWK_PROVIDER_URL).toURL())
            .cached(10, 24, TimeUnit.HOURS)
            .build()

    install(Authentication) {
        jwt(JWT_NAME) {
            verifier(jwkProvider, JWT_PROVIDER_ISSUER_URL) {
                acceptLeeway(3)
            }
            validate { credential ->
                if (credential.payload.audience.contains(FIREBASE_PROJECT_ID)) {
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
        SchemaUtils.create(TransactionsTable)
    }
}

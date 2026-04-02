package com.dmytroherez.savingstrack.server

import com.auth0.jwk.JwkProviderBuilder
import com.dmytroherez.savingstrack.server.data.tables.GoalsTable
import com.dmytroherez.savingstrack.server.data.tables.TransactionsTable
import com.dmytroherez.savingstrack.server.di.appModule
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.http.HttpStatusCode
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
import io.ktor.server.resources.Resources
import io.ktor.server.response.respond
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.migration.jdbc.MigrationUtils
import org.koin.ktor.plugin.Koin
import org.slf4j.event.Level
import java.net.URI
import java.util.concurrent.TimeUnit

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Resources)

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
        JwkProviderBuilder(URI(Constants.JWK_PROVIDER_URL).toURL())
            .cached(10, 24, TimeUnit.HOURS)
            .build()

    install(Authentication) {
        jwt(Constants.JWT_NAME) {
            verifier(jwkProvider, Constants.JWT_PROVIDER_ISSUER_URL) {
                acceptLeeway(3)
            }
            validate { credential ->
                if (credential.payload.audience.contains(Constants.FIREBASE_PROJECT_ID)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Invalid or missing token")
            }
        }
    }
}

private fun initDatabase() {
    val flyway = Flyway.configure()
        .dataSource(
            System.getenv("DB_URL"),
            System.getenv("DB_USER"),
            System.getenv("DB_PASSWORD")
        )
        .baselineOnMigrate(true)
        .load()

    flyway.migrate()

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
        val sqlStatements = MigrationUtils.statementsRequiredForDatabaseMigration(
            TransactionsTable, GoalsTable
        )

        println("=== SQL FOR FLYWAY ===")
        sqlStatements.forEach { println("$it;") }
        println("================================")
    }
}

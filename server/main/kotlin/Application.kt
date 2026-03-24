package com.savingstrack

import com.savingstrack.repo.SavingsRepository
import com.savingstrack.routing.postSaving
import com.savingstrack.tables.SavingsTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    initDatabase()

    configureRouting()
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

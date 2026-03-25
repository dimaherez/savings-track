package com.dmytroherez.savingstrack.core.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val httpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true // Prevents crashes if the server sends extra fields
            prettyPrint = true
            isLenient = true
        })
    }
}
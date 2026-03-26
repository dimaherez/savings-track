package com.dmytroherez.savingstrack.core.network

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(baseUrl: String) = HttpClient {

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true // Prevents crashes if the server sends extra fields
            prettyPrint = true
            isLenient = true
        })
    }

    defaultRequest {
        url(baseUrl)
        contentType(ContentType.Application.Json)
    }

    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.ALL
    }

    install(Auth) {
        bearer {
            loadTokens {
                val user = Firebase.auth.currentUser
                println("AUTH loadTokens: currentUser=${user?.uid}, getting token...")
                val token = user?.getIdToken(forceRefresh = false)
                println("AUTH loadTokens: token=${if (token != null) "obtained (${token.take(20)}...)" else "NULL"}")
                if (token != null) {
                    BearerTokens(accessToken = token, refreshToken = "")
                } else {
                    null
                }
            }
            refreshTokens {
                val user = Firebase.auth.currentUser
                println("AUTH refreshTokens: currentUser=${user?.uid}, force-refreshing token...")
                val token = user?.getIdToken(forceRefresh = true)
                println("AUTH refreshTokens: token=${if (token != null) "obtained (${token.take(20)}...)" else "NULL"}")
                if (token != null) {
                    BearerTokens(accessToken = token, refreshToken = "")
                } else {
                    null
                }
            }
        }
    }
}
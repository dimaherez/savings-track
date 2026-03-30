package com.dmytroherez.savingstrack

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

suspend inline fun RoutingContext.withSecureUid(
    crossinline block: suspend (String) -> Unit
) {
    val uid = call.principal<JWTPrincipal>()?.payload?.subject

    if (uid == null) {
        call.respond(HttpStatusCode.Unauthorized, "Invalid or missing token")
        return
    }

    block(uid)
}

suspend fun <T> dbQuery(block: suspend () -> T): T = withContext(Dispatchers.IO) {
    suspendTransaction { block() }
}
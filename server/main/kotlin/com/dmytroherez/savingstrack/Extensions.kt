package com.dmytroherez.savingstrack

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext

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
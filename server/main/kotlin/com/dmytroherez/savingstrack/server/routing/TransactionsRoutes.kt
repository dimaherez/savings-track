package com.dmytroherez.savingstrack.server.routing

import com.dmytroherez.savingstrack.server.Constants.JWT_NAME
import com.dmytroherez.savingstrack.server.domain.repo.TransactionsRepo
import com.dmytroherez.savingstrack.shared.dto.transactions.PostTransactionRequest
import com.dmytroherez.savingstrack.shared.routes.TransactionsRoute
import com.dmytroherez.savingstrack.server.withSecureUid
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.resources.post
import org.koin.ktor.ext.inject

fun Routing.transactionsRoutes() {
    val repository by inject<TransactionsRepo>()

    authenticate(JWT_NAME) {
        post<TransactionsRoute> {
            withSecureUid { uid ->
                repository.addTransaction(uid, call.receive<PostTransactionRequest>())
                call.respond(HttpStatusCode.Created)
            }
        }

        get<TransactionsRoute.GetByCurrency> { resource ->
            withSecureUid { uid ->
                call.respond(
                    status = HttpStatusCode.OK,
                    message = repository.getAllTransactions(
                        userId = uid,
                        currency = resource.currency
                    )
                )
            }
        }

        get<TransactionsRoute.Dashboard> {
            withSecureUid { uid ->
                call.respond(
                    status = HttpStatusCode.OK,
                    message = repository.getTransactionsDashboard(uid)
                )
            }
        }
    }
}

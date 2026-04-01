package com.dmytroherez.savingstrack.routing

import com.dmytroherez.savingstrack.Constants.FIELD_CURRENCY
import com.dmytroherez.savingstrack.Constants.JWT_NAME
import com.dmytroherez.savingstrack.domain.repo.TransactionsRepo
import com.dmytroherez.savingstrack.dto.transactions.PostTransactionRequest
import com.dmytroherez.savingstrack.routes.RoutePath.PATH_TRANSACTIONS_DASHBOARD
import com.dmytroherez.savingstrack.routes.RoutePath.PATH_SUFFIX_LIST
import com.dmytroherez.savingstrack.routes.RoutePath.PATH_SUFFIX_ADD
import com.dmytroherez.savingstrack.routes.RoutePath.PATH_TRANSACTIONS_PREFIX
import com.dmytroherez.savingstrack.withSecureUid
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Routing.transactionsRoutes() {
    val repository by inject<TransactionsRepo>()

    authenticate(JWT_NAME) {
        route(PATH_TRANSACTIONS_PREFIX) {

            post(PATH_SUFFIX_ADD) {
                withSecureUid { uid ->
                    repository.addTransaction(uid, call.receive<PostTransactionRequest>())
                    call.respond(HttpStatusCode.Created)
                }
            }

            get("$PATH_SUFFIX_LIST/{$FIELD_CURRENCY}") {
                withSecureUid { uid ->
                    call.pathParameters[FIELD_CURRENCY]?.let { currency ->
                        call.respond(
                            status = HttpStatusCode.OK,
                            message = repository.getAllTransactions(
                                userId = uid,
                                currency = currency
                            )
                        )
                    } ?: call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = "Currency in path parameters is invalid or missing"
                    )
                }
            }

            get(PATH_TRANSACTIONS_DASHBOARD) {
                withSecureUid { uid ->
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = repository.getTransactionsDashboard(uid)
                    )
                }
            }
        }
    }
}

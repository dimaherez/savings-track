package com.dmytroherez.savingstrack.routing

import com.dmytroherez.savingstrack.Constants.JWT_NAME
import com.dmytroherez.savingstrack.domain.repo.TransactionsRepo
import com.dmytroherez.savingstrack.dto.savings.PostTransactionRequest
import com.dmytroherez.savingstrack.routes.RoutePath.PATH_TRANSACTIONS_DASHBOARD
import com.dmytroherez.savingstrack.routes.RoutePath.PATH_TRANSACTIONS_LIST_TRANSACTIONS
import com.dmytroherez.savingstrack.routes.RoutePath.PATH_TRANSACTIONS_POST_TRANSACTION
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

            post(PATH_TRANSACTIONS_POST_TRANSACTION) {
                withSecureUid { uid ->
                    repository.addTransaction(uid, call.receive<PostTransactionRequest>())
                    call.respond(HttpStatusCode.Created)
                }
            }

            get(PATH_TRANSACTIONS_LIST_TRANSACTIONS) {
                withSecureUid { uid ->
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = repository.getAllTransactions(uid)
                    )
                }
            }

            get(PATH_TRANSACTIONS_DASHBOARD) {
                withSecureUid { uid ->

                }
            }
        }
    }
}

package com.dmytroherez.savingstrack.shared.routes

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/transactions")
object TransactionsRoute {
    @Serializable
    @Resource("/list/{currency}")
    class GetByCurrency(
        val parent: TransactionsRoute = TransactionsRoute,
        val currency: String
    )

    @Serializable
    @Resource("/dashboard")
    class Dashboard(
        val parent: TransactionsRoute = TransactionsRoute,
    )
}
package com.dmytroherez.savingstrack.shared.routes

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/goals")
object GoalsRoute {
    @Serializable
    @Resource("/available")
    class Available(val parent: GoalsRoute = GoalsRoute)

    @Serializable
    @Resource("/list")
    class ListAll(val parent: GoalsRoute = GoalsRoute)

    @Serializable
    @Resource("/add")
    class Add(val parent: GoalsRoute = GoalsRoute)

    @Serializable
    @Resource("/{goalId}/complete")
    class Complete(
        val parent: GoalsRoute = GoalsRoute,
        val goalId: Int
    )
}
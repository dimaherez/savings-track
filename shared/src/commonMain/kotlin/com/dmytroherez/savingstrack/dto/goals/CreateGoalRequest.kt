package com.dmytroherez.savingstrack.dto.goals

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class CreateGoalRequest(
    val title: String,
    val targetAmount: Double,
    val currency: String,
    val deadline: LocalDate? = null
)

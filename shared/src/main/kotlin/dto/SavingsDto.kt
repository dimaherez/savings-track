package org.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class SavingsDto(
    val id: Int? = null,
    val currency: String,
    val amount: Double,
    val description: String? = null
)
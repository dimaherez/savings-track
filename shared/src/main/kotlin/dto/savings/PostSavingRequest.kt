package org.example.dto.savings

import kotlinx.serialization.Serializable

@Serializable
data class PostSavingRequest(
    val userId: String,
    val currency: String,
    val amount: Double,
    val description: String? = null
)
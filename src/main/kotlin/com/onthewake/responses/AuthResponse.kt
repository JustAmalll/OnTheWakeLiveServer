package com.onthewake.responses

@kotlinx.serialization.Serializable
data class AuthResponse(
    val userId: String,
    val firstName: String,
    val token: String
)
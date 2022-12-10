package com.onthewake.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val phoneNumber: String,
    val password: String
)
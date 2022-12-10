package com.onthewake.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateAccountRequest(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val password: String
)
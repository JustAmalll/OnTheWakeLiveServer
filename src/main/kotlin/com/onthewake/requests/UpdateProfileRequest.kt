package com.onthewake.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val instagram: String,
    val telegram: String,
    val dateOfBirth: String,
    val profilePictureUri: String
)

package com.onthewake.responses

@kotlinx.serialization.Serializable
data class ProfileResponse(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val telegram: String,
    val instagram: String,
    val dateOfBirth: String,
    val profilePictureUri: String
)

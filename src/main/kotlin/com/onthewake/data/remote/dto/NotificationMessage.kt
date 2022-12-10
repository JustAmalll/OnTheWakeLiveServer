package com.onthewake.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class NotificationMessage(
    val en: String,
    val ru: String
)
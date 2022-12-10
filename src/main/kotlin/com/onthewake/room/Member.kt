package com.onthewake.room

import io.ktor.websocket.*

data class Member(
    val firstName: String,
    val sessionId: String,
    val socket: WebSocketSession
)

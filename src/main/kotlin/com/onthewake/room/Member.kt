package com.onthewake.room

import io.ktor.websocket.*

data class Member(
    val sessionId: String,
    val socket: WebSocketSession
)

package com.onthewake.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.onthewake.routes.userId
import com.onthewake.security.token.TokenConfig
import com.onthewake.sessions.QueueSession
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.sessions.*
import io.ktor.util.*

fun Application.configureSecurity(config: TokenConfig) {

    install(Sessions) {
        cookie<QueueSession>("SESSION")
    }

    intercept(ApplicationCallPipeline.Features) {
        if (call.sessions.get<QueueSession>() == null) {
            val firstName = call.parameters["firstName"] ?: "Guest"
            call.sessions.set(QueueSession(firstName, generateNonce()))
        }
    }

    authentication {
        jwt {
            realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
            verifier(
                JWT
                    .require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(config.audience)) {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
    }
}

val JWTPrincipal.userId: String?
    get() = getClaim("userId", String::class)

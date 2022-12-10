package com.onthewake

import com.onthewake.di.mainModule
import com.onthewake.plugins.*
import com.onthewake.security.token.TokenConfig
import io.ktor.server.application.*
import io.ktor.util.*
import org.koin.ktor.plugin.Koin
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@InternalAPI
@Suppress("unused")
fun Application.module() {

    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )

    install(Koin) { modules(mainModule) }
    configureSerialization()
    configureSockets()
    configureMonitoring()
    configureSecurity(tokenConfig)
    configureRouting(tokenConfig)
}

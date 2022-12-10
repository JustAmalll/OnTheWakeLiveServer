package com.onthewake.plugins

import com.onthewake.data.remote.OneSignalServiceImpl
import com.onthewake.data.trick_list.TrickListSource
import com.onthewake.data.user.UserDataSource
import com.onthewake.room.RoomController
import com.onthewake.routes.*
import com.onthewake.security.hashing.SHA256HashingService
import com.onthewake.security.token.JwtTokenService
import com.onthewake.security.token.TokenConfig
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.json.*
import io.ktor.client.plugins.kotlinx.serializer.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import org.koin.ktor.ext.inject

@Suppress("DEPRECATION")
@InternalAPI
fun Application.configureRouting(
    tokenConfig: TokenConfig
) {

    val roomController by inject<RoomController>()
    val userDataSource by inject<UserDataSource>()
    val trickListSource by inject<TrickListSource>()

    val hashingService = SHA256HashingService()
    val tokenService = JwtTokenService()

    val client = HttpClient(CIO) {
        install(JsonPlugin) {
            serializer = KotlinxSerializer()
        }
    }

    val apiKey = environment.config.property("onesignal.api_key").getString()
    val service = OneSignalServiceImpl(client, apiKey)

    routing {
        signIn(userDataSource, hashingService, tokenService, tokenConfig)
        signUp(hashingService, userDataSource)
        authenticate()
        getSecretInfo()

        queueSocket(roomController, userDataSource)
        getQueue(roomController)
        deleteQueueItem(roomController, service)

        updateUserProfile(userDataSource, roomController)
        getUserProfile(userDataSource)
        getQueueItemDetails(roomController)

        checkIfUserAlreadyExists(userDataSource)

        getTrickList()
        getUsersTrickList(trickListSource)
        addTrickList(trickListSource)

        static {
            resources("static")
        }
    }

    routing {
        get("/") {
            call.respond(HttpStatusCode.OK, "Welcome!")
        }
    }
}

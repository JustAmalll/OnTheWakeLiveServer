package com.onthewake.routes

import com.onthewake.data.model.User
import com.onthewake.data.user.UserDataSource
import com.onthewake.requests.AuthRequest
import com.onthewake.requests.CreateAccountRequest
import com.onthewake.responses.AuthResponse
import com.onthewake.security.hashing.HashingService
import com.onthewake.security.hashing.SaltedHash
import com.onthewake.security.token.TokenClaim
import com.onthewake.security.token.TokenConfig
import com.onthewake.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.signUp(
    hashingService: HashingService,
    userDataSource: UserDataSource
) {
    post("signup") {
        val request = call.receiveOrNull<CreateAccountRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = User(
            firstName = request.firstName,
            lastName = request.lastName,
            phoneNumber = request.phoneNumber,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )

        val wasAcknowledged = userDataSource.insertUser(user)
        if (!wasAcknowledged) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        call.respond(HttpStatusCode.OK)
    }
}

fun Route.signIn(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("signin") {
        val request = call.receiveOrNull<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userDataSource.getUserByPhoneNumber(request.phoneNumber)
        if (user == null) {
            call.respond(HttpStatusCode.Conflict, "Incorrect phone number or password")
            return@post
        }

        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(hash = user.password, salt = user.salt)
        )
        if (!isValidPassword) {
            call.respond(HttpStatusCode.Conflict, user.password)
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(name = "userId", value = user.id)
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(
                firstName = user.firstName,
                userId = user.id,
                token = token
            )
        )
    }
}

fun Route.authenticate() {
    authenticate {
        get("authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.checkIfUserAlreadyExists(
    userDataSource: UserDataSource
) {
    get("/checkIfUserAlreadyExists") {
        val phoneNumber = call.request.queryParameters["phoneNumber"] ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val userWithThisPhoneNumberAlreadyExists = userDataSource.getUserByPhoneNumber(phoneNumber)
        if (userWithThisPhoneNumberAlreadyExists != null) {
            call.respond(
                HttpStatusCode.Conflict, "User with this phone number already exists"
            )
            return@get
        } else call.respond(HttpStatusCode.OK)
    }
}

fun Route.getSecretInfo() {
    authenticate {
        get("secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(HttpStatusCode.OK, "Your userId is $userId")
        }
    }
}


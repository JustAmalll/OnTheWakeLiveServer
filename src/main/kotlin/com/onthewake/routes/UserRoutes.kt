package com.onthewake.routes

import com.onthewake.data.user.UserDataSource
import com.onthewake.requests.UpdateProfileRequest
import com.onthewake.responses.BasicApiResponse
import com.onthewake.responses.ProfileResponse
import com.onthewake.room.RoomController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getUserProfile(userDataSource: UserDataSource) {
    authenticate {
        get("/api/user/profile") {
            val user = userDataSource.getUserById(call.userId)
            if (user == null) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = "The user couldn't be found."
                    )
                )
                return@get
            }
            val profileResponse = ProfileResponse(
                userId = user.id,
                firstName = user.firstName,
                lastName = user.lastName,
                profilePictureUri = user.profilePictureUri,
                phoneNumber = user.phoneNumber,
                telegram = user.telegram,
                instagram = user.instagram,
                dateOfBirth = user.dateOfBirth
            )
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(successful = true, data = profileResponse)
            )
        }
    }
}

fun Route.updateUserProfile(userDataSource: UserDataSource, roomController: RoomController) {
    authenticate {
        put("/api/user/update") {
            val updateProfileRequest = call.receiveOrNull<UpdateProfileRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }

            roomController.updateQueueItem(
                userId = call.userId,
                updateProfileRequest = updateProfileRequest
            )
            val updateUserAcknowledged = userDataSource.updateUser(
                userId = call.userId,
                updateProfileRequest = updateProfileRequest
            )
            if (updateUserAcknowledged) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(successful = true)
                )
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}

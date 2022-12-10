package com.onthewake.routes

import com.onthewake.data.model.Queue
import com.onthewake.data.remote.OneSignalService
import com.onthewake.data.remote.dto.Notification
import com.onthewake.data.remote.dto.NotificationMessage
import com.onthewake.data.user.UserDataSource
import com.onthewake.responses.BasicApiResponse
import com.onthewake.room.RoomController
import com.onthewake.sessions.QueueSession
import com.onthewake.utils.Constants.ADMIN_IDS
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Route.queueSocket(roomController: RoomController, userDataSource: UserDataSource) {
    authenticate {
        webSocket("/queue-socket") {
            val session = call.sessions.get<QueueSession>()
            if (session == null) {
                close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session."))
                return@webSocket
            }
            try {
                roomController.onJoin(
                    firstName = session.firstName,
                    sessionId = session.sessionId,
                    socket = this
                )

                incoming.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        val receivedText = frame.readText().split("/")

                        val user = userDataSource.getUserById(call.userId)
                        if (user == null) {
                            call.respond(
                                HttpStatusCode.NotFound,
                                BasicApiResponse<Unit>(
                                    successful = false,
                                    message = "The user couldn't be found."
                                )
                            )
                            return@webSocket
                        }

                        roomController.addToQueue(
                            Queue(
                                userId = call.userId,
                                isLeftQueue = receivedText[0].toBoolean(),
                                firstName = receivedText[1],
                                lastName = user.lastName,
                                profilePictureUri = user.profilePictureUri,
                                timestamp = receivedText[2].toLong(),
                            )
                        )

                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                roomController.tryDisconnect(session.firstName)
            }
        }
    }
}

fun Route.getQueue(roomController: RoomController) {
    authenticate {
        get("/queue") {
            call.respond(
                status = HttpStatusCode.OK,
                message = roomController.getQueue()
            )
        }
    }
}

fun Route.deleteQueueItem(
    roomController: RoomController,
    service: OneSignalService
) {
    authenticate {
        delete("/queue/item/delete") {
            val queueItemId = call.parameters["queueItemId"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val queueItem = roomController.getQueueItemById(queueItemId)
            if (queueItem == null) {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }

            if (queueItem.userId == call.userId || call.userId in ADMIN_IDS) {
                roomController.deleteQueueItem(queueItemId)

                val queue = roomController.getQueue().sortedWith(compareBy { it.timestamp })

                val sortedLeftQueue = queue.filter { it.isLeftQueue }
                val sortedRightQueue = queue.filter { !it.isLeftQueue }

                val userInLeftQueueToShowNotification = if (sortedLeftQueue.size >= 2)
                    sortedLeftQueue.elementAt(1).userId else "0"
                val userInRightQueueToShowNotification = if (sortedRightQueue.size >= 2)
                    sortedRightQueue.elementAt(1).userId else "0"

                if (queueItem.isLeftQueue) {
                    if (userInLeftQueueToShowNotification !in ADMIN_IDS) {
                        sendNotification(userId = userInLeftQueueToShowNotification, service = service)
                    }
                } else {
                    if (userInRightQueueToShowNotification !in ADMIN_IDS) {
                        sendNotification(userId = userInRightQueueToShowNotification, service = service)
                    }
                }

                call.respond(HttpStatusCode.OK, queueItem)
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}

suspend fun sendNotification(userId: String, service: OneSignalService) {
    service.sendNotification(
        Notification(
            includeExternalUserIds = listOf(userId),
            headings = NotificationMessage(
                en = "It's time to ride!",
                ru = "Пора катать!"
            ),
            contents = NotificationMessage(
                en = "The queue has almost reached you, you are next!",
                ru = "Очередь почти дошла до вас, вы следующий!"
            ),
            appId = OneSignalService.ONESIGNAL_APP_ID
        )
    )
}

fun Route.getQueueItemDetails(roomController: RoomController) {
    get("/queue/item/details") {
        val queueItemId = call.parameters["queueItemId"] ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val queueItem = roomController.getQueueItemDetails(queueItemId) ?: kotlin.run {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }
        call.respond(
            HttpStatusCode.OK,
            BasicApiResponse(successful = true, data = queueItem)
        )
    }
}
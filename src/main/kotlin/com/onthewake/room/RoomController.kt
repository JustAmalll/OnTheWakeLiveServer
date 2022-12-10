package com.onthewake.room

import com.onthewake.data.model.Queue
import com.onthewake.data.queue.QueueDataSource
import com.onthewake.requests.UpdateProfileRequest
import com.onthewake.responses.ProfileResponse
import com.onthewake.responses.QueueResponse
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController(
    private val queueDataSource: QueueDataSource
) {
    private val members = ConcurrentHashMap<String, Member>()

    fun onJoin(
        firstName: String,
        sessionId: String,
        socket: WebSocketSession
    ) {
        members[firstName] = Member(
            firstName = firstName,
            sessionId = sessionId,
            socket = socket
        )
    }

    suspend fun getQueue(): List<Queue> = queueDataSource.getQueue()

    suspend fun updateQueueItem(
        userId: String,
        updateProfileRequest: UpdateProfileRequest
    ): Boolean = queueDataSource.updateQueueItem(
        userId, updateProfileRequest
    )

    suspend fun getQueueItemDetails(queueItemId: String): ProfileResponse? =
        queueDataSource.getQueueItemDetails(queueItemId)

    suspend fun addToQueue(queue: Queue) {

        queueDataSource.addToQueue(queue = queue)

        members.values.forEach { member ->
            member.socket.send(
                Frame.Text(
                    Json.encodeToString(QueueResponse(isDeleteAction = false, queue = queue))
                )
            )
        }
    }

    suspend fun getQueueItemById(
        queueItemId: String
    ) = queueDataSource.getQueueItemById(queueItemId)

    suspend fun deleteQueueItem(
        queueItemId: String
    ) {
        val deletedQueueItem = getQueueItemById(queueItemId)
        queueDataSource.deleteQueueItem(queueItemId)

        members.values.forEach { member ->
            member.socket.send(
                Frame.Text(
                    Json.encodeToString(
                        deletedQueueItem?.let {
                            QueueResponse(isDeleteAction = true, queue = it)
                        }
                    )
                )
            )
        }
    }

    suspend fun tryDisconnect(username: String) {
        members[username]?.socket?.close()
        if (members.containsKey(username)) members.remove(username)
    }
}
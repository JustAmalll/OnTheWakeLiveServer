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
        sessionId: String,
        socket: WebSocketSession
    ) {
        members[sessionId] = Member(
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

    suspend fun addToQueue(queueItem: Queue) {

        val queue = getQueue()
        val isUserAlreadyInQueue = queue.find { it.userId == queueItem.userId }

        if (isUserAlreadyInQueue == null) {
            queueDataSource.addToQueue(queue = queueItem)

            members.values.forEach { member ->
                member.socket.send(
                    Frame.Text(
                        Json.encodeToString(QueueResponse(isDeleteAction = false, queueItem = queueItem))
                    )
                )
            }
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
                            QueueResponse(isDeleteAction = true, queueItem = it)
                        }
                    )
                )
            )
        }
    }

    suspend fun tryDisconnect(sessionId: String) {
        members[sessionId]?.socket?.close()
        if (members.containsKey(sessionId)) members.remove(sessionId)
    }
}
package com.onthewake.data.queue

import com.onthewake.data.model.Queue
import com.onthewake.requests.UpdateProfileRequest
import com.onthewake.responses.ProfileResponse

interface QueueDataSource {
    suspend fun getQueue(): List<Queue>
    suspend fun getQueueItemDetails(queueItemId: String): ProfileResponse?
    suspend fun addToQueue(queue: Queue)
    suspend fun updateQueueItem(
        userId: String,
        updateProfileRequest: UpdateProfileRequest
    ): Boolean
    suspend fun getQueueItemById(queueItemId: String): Queue?
    suspend fun getQueueItemByUserId(userId: String): Queue?
    suspend fun deleteQueueItem(queueItemId: String)
}
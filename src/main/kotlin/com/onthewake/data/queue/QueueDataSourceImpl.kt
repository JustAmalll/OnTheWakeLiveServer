package com.onthewake.data.queue

import com.onthewake.data.model.Queue
import com.onthewake.data.model.User
import com.onthewake.requests.UpdateProfileRequest
import com.onthewake.responses.ProfileResponse
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class QueueDataSourceImpl(
    db: CoroutineDatabase
) : QueueDataSource {

    private val queue = db.getCollection<Queue>()
    private val users = db.getCollection<User>()

    override suspend fun getQueue(): List<Queue> = queue.find()
        .descendingSort(Queue::timestamp)
        .toList()

    override suspend fun getQueueItemDetails(queueItemId: String): ProfileResponse? {
        val queueItem = queue.findOneById(queueItemId) ?: return null
        val user = users.findOneById(queueItem.userId) ?: return null
        return ProfileResponse(
            userId = user.id,
            firstName = user.firstName,
            lastName = user.lastName,
            phoneNumber = user.phoneNumber,
            profilePictureUri = user.profilePictureUri,
            telegram = user.telegram,
            instagram = user.instagram,
            dateOfBirth = user.dateOfBirth
        )
    }

    override suspend fun addToQueue(queue: Queue) {
        this.queue.insertOne(queue)
    }

    override suspend fun updateQueueItem(
        userId: String,
        updateProfileRequest: UpdateProfileRequest
    ): Boolean {
        val queueItem = getQueueItemByUserId(userId) ?: return false
        return queue.updateOneById(
            id = queueItem.id,
            update = Queue(
                id = queueItem.id,
                userId = queueItem.userId,
                firstName = updateProfileRequest.firstName,
                lastName = updateProfileRequest.lastName,
                profilePictureUri = updateProfileRequest.profilePictureUri,
                isLeftQueue = queueItem.isLeftQueue,
                timestamp = queueItem.timestamp
            )
        ).wasAcknowledged()
    }

    override suspend fun getQueueItemById(
        queueItemId: String
    ): Queue? = queue.findOneById(queueItemId)

    override suspend fun getQueueItemByUserId(
        userId: String
    ): Queue? = queue.findOne(Queue::userId eq userId)

    override suspend fun deleteQueueItem(queueItemId: String) {
        queue.deleteOneById(queueItemId)
    }

}
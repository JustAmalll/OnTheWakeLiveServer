package com.onthewake.data.user

import com.onthewake.data.model.User
import com.onthewake.requests.UpdateProfileRequest
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoUserDataSource(
    db: CoroutineDatabase
) : UserDataSource {

    private val users = db.getCollection<User>()

    override suspend fun getUserByPhoneNumber(phoneNumber: String): User? {
        return users.findOne(User::phoneNumber eq phoneNumber)
    }

    override suspend fun getUserById(id: String): User? {
        return users.findOneById(id)
    }

    override suspend fun insertUser(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }

    override suspend fun updateUser(
        userId: String,
        updateProfileRequest: UpdateProfileRequest
    ): Boolean {
        val user = getUserById(userId) ?: return false
        return users.updateOneById(
            id = userId,
            update = User(
                id = user.id,
                firstName = updateProfileRequest.firstName,
                lastName = updateProfileRequest.lastName,
                profilePictureUri = updateProfileRequest.profilePictureUri,
                instagram = updateProfileRequest.instagram,
                telegram = updateProfileRequest.telegram,
                phoneNumber = updateProfileRequest.phoneNumber,
                dateOfBirth = updateProfileRequest.dateOfBirth,
                password = user.password,
                salt = user.salt
            )
        ).wasAcknowledged()
    }
}
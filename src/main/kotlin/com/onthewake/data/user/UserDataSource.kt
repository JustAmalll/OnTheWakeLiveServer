package com.onthewake.data.user

import com.onthewake.data.model.User
import com.onthewake.requests.UpdateProfileRequest

interface UserDataSource {
    suspend fun getUserByPhoneNumber(phoneNumber: String): User?
    suspend fun getUserById(id: String): User?
    suspend fun insertUser(user: User): Boolean
    suspend fun updateUser(
        userId: String,
        updateProfileRequest: UpdateProfileRequest
    ): Boolean

}